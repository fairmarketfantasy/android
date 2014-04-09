package com.fantasysport.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fantasysport.R;
import com.fantasysport.activities.MainActivity;
import com.fantasysport.models.Market;
import com.fantasysport.models.Roster;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.requestListeners.RosterResponseListener;

import java.util.List;

/**
 * Created by bylynka on 3/14/14.
 */
public class PlayersFragment extends BasePlayersFragment implements MainActivity.IOnMarketsListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity)getMainActivity()).addOnMarketsListener(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void loadPlayers(final String position){
        Market market = getMarket();
        if(market == _lastMarket && _lastPosition == position
                && _lastBenchedState == canBenched() && !_pullToRefreshLayout.isRefreshing()
                || market == null){
            return;
        }
        showProgress();
        _lastMarket = market;
        _lastPosition = position;
        _lastBenchedState = canBenched();
        if (getRoster() == null) {
            getWebProxy().createRoster(market.getId(), new RosterResponseListener() {
                @Override
                public void onRequestError(RequestError message) {
                    dismissProgress();
                    showAlert(getString(R.string.error), getString(R.string.unknown_error));
                }
                @Override
                public void onRequestSuccess(Roster roster) {
                    setRoster(roster);
                    getWebProxy().getPlayers(position, canBenched(), getRoster().getId(), _playersListener);
                }
            });
        } else {
            getWebProxy().getPlayers(position, canBenched(), getRoster().getId(), _playersListener);
        }
    }

    @Override
    public void onRosterLoaded(Roster roster) {

    }

    @Override
    public void onMarkets(List<Market> markets) {
        updateMarkets();
        if(markets == null || markets.size() == 0){
            _playersAdapter.setItems(null);
            _playersAdapter.notifyDataSetInvalidated();
            return;
        }
        _pager.setCurrentItem(0 , false);
        getFragmentMediator().changeMarket(this, markets.get(0));
    }
}
