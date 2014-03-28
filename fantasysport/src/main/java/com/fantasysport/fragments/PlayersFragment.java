package com.fantasysport.fragments;

import android.view.View;
import com.fantasysport.R;
import com.fantasysport.models.Market;
import com.fantasysport.models.Roster;
import com.fantasysport.webaccess.WebProxy;
import com.fantasysport.webaccess.requestListeners.RosterResponseListener;
import com.fantasysport.webaccess.requestListeners.RequestError;

/**
 * Created by bylynka on 3/14/14.
 */
public class PlayersFragment extends BasePlayersFragment{

    public PlayersFragment(WebProxy proxy, MainFragmentMediator fragmentMediator){
        super(proxy,fragmentMediator);
    }

    @Override
    protected void loadPlayers(final String position, boolean showProgress){
        Market market = getMarket();
        if(market == _lastMarket && _lastPosition == position
                && _lastBenchedState == canBenched() && !_pullToRefreshLayout.isRefreshing()){
            return;
        }
        if(showProgress){
            showProgress();
        }
        _lastMarket = market;
        _lastPosition = position;
        _lastBenchedState = canBenched();
        if (getRoster() == null) {
            _webProxy.createRoster(market.getId(), new RosterResponseListener() {
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
    public void onRosterLoaded(Roster roster) {

    }
}
