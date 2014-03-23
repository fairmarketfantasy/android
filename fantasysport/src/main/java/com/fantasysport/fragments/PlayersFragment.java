package com.fantasysport.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.activities.IndividuaPredictionsActivity;
import com.fantasysport.adapters.CandidatePlayersAdapter;
import com.fantasysport.models.Market;
import com.fantasysport.models.Player;
import com.fantasysport.models.Roster;
import com.fantasysport.views.PositionView;
import com.fantasysport.webaccess.WebProxy;
import com.fantasysport.webaccess.requestListeners.AddPlayerResponseListener;
import com.fantasysport.webaccess.requestListeners.CreateRosterResponseListener;
import com.fantasysport.webaccess.requestListeners.PlayersResponseListener;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.responses.PlayersRequestResponse;

/**
 * Created by bylynka on 3/14/14.
 */
public class PlayersFragment extends MainActivityFragment implements PositionView.OnPositionSelecteListener, CandidatePlayersAdapter.IListener,
                                    MainFragmentMediator.IPlayerPositionListener, MainFragmentMediator.IOnBenchedStateChangedListener{

    private PositionView _positionView;
    private CandidatePlayersAdapter _playersAdapter;
    private Market _lastMarket;
    private String _lastPosition;
    private boolean _lastBenchedState;

    public PlayersFragment(WebProxy proxy){
        super(proxy);
        _fragmentMediator.addPlayerPositionListener(this);
        _fragmentMediator.addOnBenchedStateChangedListener(this);
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
        initHeaderView();
        _positionView = getViewById(R.id.position_view);
        _positionView.setPositionListener(this);
        _positionView.setPositions(_storage.getDefaultRosterData().getPositions());
        setPlayersListView();
        Roster roster = getRoster();
        setMoneyTxt(roster != null? roster.getRemainingSalary(): _storage.getDefaultRosterData().getRemainingSalary());
        loadPlayers(_positionView.getPosition(), false);
    }

    @Override
    protected void setPager(int id) {
        super.setPager(id);
        int position = _pagerAdapter.getMarketPosition(getMarket());
        if(position < 0){
            return;
        }
        _pager.setCurrentItem(position , false);
        _pagerAdapter.notifyDataSetChanged();
        _pager.invalidate();
    }

    @Override
    public void onMarketChanged(Object sender, Market market) {
        setRoster(null);
        loadPlayers(_positionView.getPosition(), true);
        int position = _pagerAdapter.getMarketPosition(market);
        int curPossition = _pager.getCurrentItem();
        if(sender == this || position < 0 || position == curPossition){
            return;
        }
        _pager.setCurrentItem(position , false);
    }

    private void setPlayersListView() {
        ListView listView = getViewById(R.id.players_list);
        _playersAdapter = new CandidatePlayersAdapter(getActivity());
        _playersAdapter.setListener(this);
        listView.setAdapter(_playersAdapter);
    }

    @Override
    public void positionSelected(String position) {
        loadPlayers(position, true);
    }

    private void loadPlayers(final String position, boolean showProgress){
        Market market = getMarket();
        if(market == _lastMarket && _lastPosition == position
                && _lastBenchedState == canBenched()){
            return;
        }
        if(showProgress){
            showProgress();
        }
        _lastMarket = market;
        _lastPosition = position;
        _lastBenchedState = canBenched();
        if (getRoster() == null) {
            _webProxy.createRoster(market.getId(), new CreateRosterResponseListener() {
                @Override
                public void onRequestError(RequestError message) {
                    dismissProgress();
                    showAlert(getString(R.string.error), getString(R.string.unknown_error));
                }
                @Override
                public void onRequestSuccess(Roster roster) {
                    setRoster(roster);
                    _webProxy.getPlayers(position, canBenched(), getRoster().getId(), _playersListener);
                }
            });
        } else {
            _webProxy.getPlayers(position, canBenched(), getRoster().getId(), _playersListener);
        }
    }

    @Override
    public void onAddPlayer(Player player) {
        showProgress();
        _webProxy.addPlayer(getRoster().getId(), player, new AddPlayerResponseListener() {
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
            }
        });
    }


    @Override
    public void onPlayerAdded(Object sender, Player player) {
        _playersAdapter.getPlayers().remove(player);
        _playersAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPT25Player(Player player) {
        Activity activity = getActivity();
        Intent intent = new Intent(activity, IndividuaPredictionsActivity.class);
        intent.putExtra(Const.PLAYER, player);
        intent.putExtra(Const.ROSTER_ID, getRoster().getId());
        intent.putExtra(Const.MARKET_ID, getMarket().getId());
        startActivity(intent);
        activity.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    PlayersResponseListener _playersListener = new PlayersResponseListener() {
        @Override
        public void onRequestError(RequestError message) {
            dismissProgress();
            showAlert(getString(R.string.error), getString(R.string.error));
        }

        @Override
        public void onRequestSuccess(PlayersRequestResponse response) {
            _playersAdapter.setItems(response.getPlayers());
            _playersAdapter.notifyDataSetInvalidated();
            dismissProgress();
        }
    };

    @Override
    public void onPageChanged(int page) {
        if(page != 1){
            return;
        }
//        loadPlayers(_positionView.getPosition(), true);
    }

    @Override
    public void onPlayerPositionChanged(Object sender, String position) {
       _positionView.setPosition(position);
    }

    @Override
    public void onBenchedStateChanged(Object sender, boolean state) {
        loadPlayers(_positionView.getPosition(), true);
    }
}
