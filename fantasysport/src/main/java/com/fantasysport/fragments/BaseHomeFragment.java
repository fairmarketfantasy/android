package com.fantasysport.fragments;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.activities.IndividuaPredictionsActivity;
import com.fantasysport.adapters.PlayerItem;
import com.fantasysport.adapters.RosterPlayersAdapter;
import com.fantasysport.models.*;
import com.fantasysport.utility.OutParameter;
import com.fantasysport.views.Switcher;
import com.fantasysport.webaccess.requestListeners.AutoFillResponseListener;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.requestListeners.RosterResponseListener;
import com.fantasysport.webaccess.requestListeners.TradePlayerResponseListener;
import com.fantasysport.webaccess.requests.AutoFillRequest;
import com.fantasysport.webaccess.responses.AutoFillResponse;
import com.fantasysport.webaccess.responses.TradePlayerResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 3/25/14.
 */
public abstract class BaseHomeFragment extends MainActivityFragment  implements AdapterView.OnItemClickListener, Switcher.ISelectedListener {

    private Switcher _switcher;
    private ListView _playersList;
    private AutoFillRequest _autoFillRequest;


    public BaseHomeFragment(){
        super();
    }

    @Override
    protected void init() {
        super.init();
        Button autofillBtn = getViewById(R.id.autofill_btn);
        autofillBtn.setOnClickListener(_autofillClickListener);
        _switcher = getViewById(R.id.switcher);
        _switcher.setSelected(true);
        _switcher.setSelectedListener(this);
        setRoster();
        setPager(321);
    }

    @Override
    protected void setPager(int id) {
        super.setPager(id);
        List<Market> markets = getMarkets();
        if (markets != null && markets.size() > 0) {
            setNewRoster(markets.get(0));
        }
    }

    @Override
    public void onMarketChanged(Object sender, Market market) {
        setNewRoster(market);
        if(_autoFillRequest != null && !_autoFillRequest.isCancelled()){
            try{
                dismissProgress();
                getWebProxy().getSpiceManager().cancel(_autoFillRequest);
            }catch (Exception e){
            }finally {
                _autoFillRequest = null;
            }

        }
        int position = _pagerAdapter.getMarketPosition(market);
        int curPossition = _pager.getCurrentItem();
        if(sender == this || position < 0 || curPossition == position){
            return;
        }
        _pager.setCurrentItem(position , false);
    }

    @Override
    public void onPlayerAdded(Object sender, Player player) {
        updatePlayersList();
        int position = _playerAdapter.getPosition(player);
        if(position > 0){
            _playersList.setSelection(position);
        }

    }

    protected void setNewRoster(Market market) {
        setMarket(market);
        setEmptyRoster();
    }

    protected void setEmptyRoster() {
        DefaultRosterData rosterData = getStorage().getDefaultRosterData();
//        setMoneyTxt(rosterData.getRemainingSalary());
        getFragmentMediator().changeRemainingSalary(this, rosterData.getRemainingSalary());
        List<IPlayer> items = _playerAdapter.getItems();
        items.clear();
        List<String> positions = rosterData.getPositions();
        for (int i = 0; i < positions.size(); i++) {
            items.add(new PlayerItem(positions.get(i)));
        }
        _playerAdapter.notifyDataSetChanged();
    }

    protected void updatePlayersList() {
        Roster roster = getRoster();
        if (roster == null) {
            return;
        }
        remainingSalaryChanged(roster.getRemainingSalary());
        List<Player> players = roster.getPlayers();
        List<IPlayer> playerItems = _playerAdapter.getItems();
        _playerAdapter.setRosterState(roster.getState());
        for (int i = 0; i < playerItems.size(); i++) {
            boolean updated = false;
            IPlayer iPlayer = playerItems.get(i);
            for (int j = 0; j < players.size(); j++) {
                Player player = players.get(j);
                if (iPlayer.getPosition().compareToIgnoreCase(player.getPosition()) == 0) {
                    playerItems.set(i, player);
                    updated = true;
                }
            }
            if (!updated && iPlayer instanceof Player) {
                playerItems.set(i, new PlayerItem(iPlayer.getPosition()));
            }
        }
        _playerAdapter.notifyDataSetChanged();
    }

    protected void setRoster() {
        _playersList = getViewById(R.id.roster_list);
        _playersList.setOnItemClickListener(this);
        List<IPlayer> items = new ArrayList<IPlayer>();
        _playerAdapter = new RosterPlayersAdapter(items, getBaseActivity(), getPredictionRoster());
        _playersList.setAdapter(_playerAdapter);
        _playerAdapter.setListener(_playerAdapterListener);
    }

    RosterPlayersAdapter.IListener _playerAdapterListener = new RosterPlayersAdapter.IListener() {
        @Override
        public void onTrade(final Player player) {
            showProgress();
            final Roster roster = getRoster();
            getWebProxy().tradePlayer(roster.getId(), player, new TradePlayerResponseListener() {
                @Override
                public void onRequestError(RequestError error) {
                    dismissProgress();
                    showAlert(getString(R.string.error), error.getMessage());
                }

                @Override
                public void onRequestSuccess(TradePlayerResponse response) {
                    double salary = roster.getRemainingSalary();
                    roster.getPlayers().remove(player);
                    salary += response.getPrice();
                    roster.setRemainingSalary(salary);
                    updatePlayersList();
                    dismissProgress();
                    getFragmentMediator().changePlayerPosition(this, player.getPosition());
                    getMainActivity().navigateToPlayers();
                }
            });
        }

        @Override
        public void onPT25Player(Player player) {
            Activity activity = getActivity();
            Intent intent = new Intent(activity, IndividuaPredictionsActivity.class);
            intent.putExtra(Const.PLAYER, player);
            intent.putExtra(Const.ROSTER_ID, getRoster().getId());
            intent.putExtra(Const.MARKET_ID, getMarket().getId());
            intent.putExtra(Const.GAME, getMarket().getGames().get(0));
            startActivity(intent);
        }
    };

    View.OnClickListener _autofillClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(getMarket() == null){
                return;
            }
            Roster roster = getRoster();
            showProgress();
            final OutParameter outPar = new OutParameter();
            if(roster == null){
                getWebProxy().createRoster(getMarket().getId(), new RosterResponseListener() {
                    @Override
                    public void onRequestError(RequestError message) {
                        dismissProgress();
                        showAlert(getString(R.string.error), getString(R.string.unknown_error));
                    }
                    @Override
                    public void onRequestSuccess(Roster roster) {
                        setRoster(roster);
                        if(getMarket() == null){
                            dismissProgress();
                            return;
                        }
                        getWebProxy().autofillRoster(getMarket().getId(), roster.getId(), _autoFillResponseListener, outPar);
                        _autoFillRequest = outPar.getParameter();
                    }
                });
            }else {
                getWebProxy().autofillRoster(getMarket().getId(), roster.getId(), _autoFillResponseListener, outPar);
                _autoFillRequest = outPar.getParameter();
            }
        }
    };

    AutoFillResponseListener _autoFillResponseListener = new AutoFillResponseListener() {
        @Override
        public void onRequestError(RequestError error) {
            _autoFillRequest = null;
            dismissProgress();
            if(error.isCanceledRequest()){
                return;
            }
            showAlert(getString(R.string.error), error.getMessage());

        }

        @Override
        public void onRequestSuccess(AutoFillResponse response) {
            _autoFillRequest = null;
            dismissProgress();
            if(response.getRoster().getMarketId() == getMarket().getId()){
                setRoster(response.getRoster());
            }
            updatePlayersList();
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        IPlayer item = (IPlayer) _playerAdapter.getItem(position);
//        if (item instanceof Player) {
//            return;
//        }
        getFragmentMediator().changePlayerPosition(this, item.getPosition());
        getMainActivity().navigateToPlayers();
    }

    @Override
    public void onStateChanged(boolean isSelected) {
        setCanBenched(isSelected);
        getFragmentMediator().changeBenchedState(this, isSelected);
        Roster roster = getRoster();
        if(roster != null && !isSelected){
            showProgress();
            getWebProxy().removeBenchedPlayers(roster.getId(), new RosterResponseListener() {
                @Override
                public void onRequestError(RequestError message) {
                    dismissProgress();
                }

                @Override
                public void onRequestSuccess(Roster roster) {
                    dismissProgress();
                    setRoster(roster);
                    updatePlayersList();
                }
            });
        }

    }

    @Override
    public void onPageChanged(int page) {
    }

    @Override
    public void onRosterLoaded(Roster roster) {
        updatePlayersList();
    }

    @Override
    public void onUpdated(Object initiator) {
        if(initiator != this){
            return;
        }
        _pullToRefreshLayout.setRefreshComplete();
    }

    @Override
    public void onRefreshStarted(View view) {
        getMainActivity().updateUserData(this);
    }
}
