package com.fantasysport.fragments.pages.fantasy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.activities.IndividuaPredictionsActivity;
import com.fantasysport.adapters.fantasy.CandidatePlayersAdapter;
import com.fantasysport.models.Market;
import com.fantasysport.models.Player;
import com.fantasysport.models.Position;
import com.fantasysport.models.Roster;
import com.fantasysport.views.IOnPositionSelectedListener;
import com.fantasysport.views.ScrollPositionControl;
import com.fantasysport.webaccess.requestListeners.AddPlayerResponseListener;
import com.fantasysport.webaccess.requestListeners.PlayersResponseListener;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.responses.PlayersRequestResponse;

/**
 * Created by bylynka on 3/24/14.
 */
public abstract class BasePlayersFragment extends MainActivityFragment implements IOnPositionSelectedListener, CandidatePlayersAdapter.IListener,
        MainFragmentMediator.IPlayerPositionListener, MainFragmentMediator.IOnBenchedStateChangedListener
        {

    protected ScrollPositionControl _positionView;
    protected CandidatePlayersAdapter _playersAdapter;
    protected Market _lastMarket;
    protected String _lastPosition;
    protected boolean _lastBenchedState;
    protected ListView _playersList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentMediator().addPlayerPositionListener(this);
        getFragmentMediator().addOnBenchedStateChangedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_players, container, false);
        init();
        return _rootView;
    }

    @Override
    protected void init() {
        super.init();
        setPager(123);
        _positionView = getViewById(R.id.position_view);
        _positionView.setPositionListener(this);
        _positionView.setPositions(getStorage().getDefaultRosterData().getPositions());
        setPlayersListView();
        Roster roster = getRoster();
        setMoneyTxt(roster != null? roster.getRemainingSalary(): getStorage().getDefaultRosterData().getRemainingSalary());
        loadPlayers(_positionView.getPosition());
    }

    @Override
    protected void setPager(int id) {
        super.setPager(id);
        int position = _pagerAdapter.getMarketPosition(getMarket());
        if(position < 0){
            return;
        }
        _pager.setCurrentItem(position, false);
        _pagerAdapter.notifyDataSetChanged();
        _pager.invalidate();
    }

    @Override
    public void onMarketChanged(Object sender, Market market) {
        setRoster(null);

        loadPlayers(_positionView.getPosition());
        int position = _pagerAdapter.getMarketPosition(market);
        int curPossition = _pager.getCurrentItem();
        if(sender == this || position < 0 || position == curPossition){
            return;
        }
        _pager.setCurrentItem(position, false);
    }

    protected void setPositionLabel(String positionName){
        TextView lbl = getViewById(R.id.position_name_lbl);
        if(lbl == null){
            return;
        }
        lbl.setText(positionName);
    }

    private void setPlayersListView() {
        _playersList = getViewById(R.id.players_list);
        _playersAdapter = new CandidatePlayersAdapter(getActivity());
        _playersAdapter.setListener(this);
        _playersList.setAdapter(_playersAdapter);
    }


    @Override
    public void positionSelected(Position position, int index) {
        loadPlayers(position);
    }

    protected abstract void loadPlayers(final Position position);

    @Override
    public void onAddPlayer(Player player) {
        Roster roster = getRoster();
        if(roster == null){
            return;
        }
        showProgress();
        getWebProxy().addPlayer(roster.getId(), player, new AddPlayerResponseListener() {
            @Override
            public void onRequestError(RequestError error) {
                dismissProgress();
                showAlert(getString(R.string.error), error.getMessage());
            }

            @Override
            public void onRequestSuccess(Player player) {
                Roster roster = getRoster();
                addPlayerToRoster(player);
                double remainingSalary = roster.getRemainingSalary() - player.getBuyPrice();
                remainingSalaryChanged(remainingSalary);
                dismissProgress();
                getBaseFFragment().navigateToRosters();
            }
        });
    }


    @Override
    public void onPlayerAdded(Object sender, Player player) {
    }

    @Override
    public void onPT25Player(Player player) {
        Roster roster = getRoster();
        if(roster == null){
            return;
        }
        Activity activity = getActivity();
        Intent intent = new Intent(activity, IndividuaPredictionsActivity.class);
        intent.putExtra(Const.PLAYER, player);
        intent.putExtra(Const.ROSTER_ID, roster.getId());
        intent.putExtra(Const.MARKET_ID, getMarket().getId());
        intent.putExtra(Const.GAME, getMarket().getGames().get(0));
        startActivity(intent);
    }

    protected PlayersResponseListener _playersListener = new PlayersResponseListener() {
        @Override
        public void onRequestError(RequestError message) {
            dismissProgress();
            showAlert(getString(R.string.error), getString(R.string.error));
            onUpdated(BasePlayersFragment.this);
        }

        @Override
        public void onRequestSuccess(PlayersRequestResponse response) {
            _playersAdapter.setItems(response.getPlayers());
            _playersAdapter.notifyDataSetInvalidated();
            dismissProgress();
            onUpdated(BasePlayersFragment.this);
        }
    };

    @Override
    public void onPageChanged(int page) {
    }

    @Override
    public void onPlayerPositionChanged(Object sender, String posAcr) {
        _positionView.setPosition(posAcr);
    }

    @Override
    public void onBenchedStateChanged(Object sender, boolean state) {
    }

    @Override
    public void onRefreshStarted(View view) {
        loadPlayers(_positionView.getPosition());
    }

    @Override
    public void onUpdated(Object initiator) {
        if(initiator != this || !_pullToRefreshLayout.isRefreshing()){
            return;
        }
        _pullToRefreshLayout.setRefreshComplete();
    }
}
