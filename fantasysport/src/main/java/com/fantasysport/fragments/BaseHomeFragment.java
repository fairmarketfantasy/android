package com.fantasysport.fragments;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.fantasysport.R;
import com.fantasysport.adapters.PlayerItem;
import com.fantasysport.adapters.RosterPlayersAdapter;
import com.fantasysport.models.*;
import com.fantasysport.views.Switcher;
import com.fantasysport.webaccess.WebProxy;
import com.fantasysport.webaccess.requestListeners.AutofillResponseListener;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.requestListeners.TradePlayerResponseListener;
import com.fantasysport.webaccess.responses.AutofillResponse;
import com.fantasysport.webaccess.responses.TradePlayerResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 3/25/14.
 */
public abstract class BaseHomeFragment extends MainActivityFragment  implements AdapterView.OnItemClickListener, Switcher.ISelectedListener {

    private Switcher _switcher;

    public BaseHomeFragment(WebProxy proxy, MainFragmentMediator fragmentMediator) {
        super(proxy, fragmentMediator);
    }

    @Override
    protected void init() {
        super.init();
        Button autofillBtn = getViewById(R.id.autofill_btn);
        autofillBtn.setTypeface(getProhibitionRound());
        autofillBtn.setOnClickListener(_autofillClickListener);
        _switcher = getViewById(R.id.switcher);
        _switcher.setSelected(true);
        _switcher.setSelectedListener(this);
        setRoster();
        setPager(321);
        initHeaderView();
    }

    @Override
    protected void setPager(int id) {
        super.setPager(id);
        List<Market> markets = getMarkets();//_storage.getMarkets();
        if (markets != null && markets.size() > 0) {
            setNewRoster(markets.get(0));
        }
    }

    @Override
    public void onMarketChanged(Object sender, Market market) {
        setNewRoster(market);
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
    }

    protected void setNewRoster(Market market) {
        setMarket(market);
        setEmptyRoster();
    }

    protected void setEmptyRoster() {
        DefaultRosterData rosterData = _storage.getDefaultRosterData();
        setMoneyTxt(rosterData.getRemainingSalary());
        List<IPlayer> items = _playerAdapter.getItems();
        items.clear();
        List<String> positions = rosterData.getPositions();
        for (int i = 0; i < positions.size(); i++) {
            items.add(new PlayerItem(positions.get(i)));
        }
        _playerAdapter.notifyDataSetChanged();
    }

    private void updatePlayersList() {
        Roster roster = getRoster();
        if (roster == null) {
            return;
        }
        remainingSalaryChanged(roster.getRemainingSalary());
        List<Player> players = roster.getPlayers();
        List<IPlayer> playerItems = _playerAdapter.getItems();
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
        ListView rosterList = getViewById(R.id.roster_list);
        rosterList.setOnItemClickListener(this);
        List<IPlayer> items = new ArrayList<IPlayer>();
        _playerAdapter = new RosterPlayersAdapter(items, getActivity(), getPredictionRoster());
        rosterList.setAdapter(_playerAdapter);
        _playerAdapter.setListener(_playerAdapterListener);
    }

    RosterPlayersAdapter.IListener _playerAdapterListener = new RosterPlayersAdapter.IListener() {
        @Override
        public void onTrade(final Player player) {
            showProgress();
            final Roster roster = getRoster();
            _webProxy.tradePlayer(roster.getId(), player, new TradePlayerResponseListener() {
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
                }
            });
        }
    };

    View.OnClickListener _autofillClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Roster roster = getRoster();
            int rosterId = roster == null ? -1 : roster.getId();
            showProgress();
            _webProxy.autofillRoster(getMarket().getId(), rosterId, new AutofillResponseListener() {
                @Override
                public void onRequestError(RequestError error) {
                    dismissProgress();
                    showAlert(getString(R.string.error), error.getMessage());
                }

                @Override
                public void onRequestSuccess(AutofillResponse response) {
                    setRoster(response.getRoster());
                    updatePlayersList();
                    dismissProgress();
                }
            });
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        IPlayer item = (IPlayer) _playerAdapter.getItem(position);
//        if (item instanceof Player) {
//            return;
//        }
        _fragmentMediator.changePlayerPosition(this, item.getPosition());
        getMainActivity().navigateToPlayers();
    }

    @Override
    public void onStateChanged(boolean isSelected) {
        setCanBenched(isSelected);
        _fragmentMediator.changeBenchedState(this, isSelected);
    }

    @Override
    public void onPageChanged(int page) {
    }

    @Override
    public void onRosterLoaded(Roster roster) {
        updatePlayersList();
    }
}