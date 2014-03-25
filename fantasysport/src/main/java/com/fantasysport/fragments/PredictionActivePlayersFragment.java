package com.fantasysport.fragments;

import com.fantasysport.models.Market;
import com.fantasysport.models.Roster;
import com.fantasysport.webaccess.WebProxy;

/**
 * Created by bylynka on 3/24/14.
 */
public class PredictionActivePlayersFragment extends BasePlayersFragment {

    public PredictionActivePlayersFragment(WebProxy proxy, MainFragmentMediator fragmentMediator) {
        super(proxy, fragmentMediator);
    }

    @Override
    protected void loadPlayers(final String position, boolean showProgress){
        Market market = getMarket();
        if(market == _lastMarket && _lastPosition == position
                && _lastBenchedState == canBenched() || getRoster() == null){
            return;
        }
        if(showProgress){
            showProgress();
        }
        _lastMarket = market;
        _lastPosition = position;
        _lastBenchedState = canBenched();
        _webProxy.getPlayers(position, canBenched(), getRoster().getId(), _playersListener);
    }

    @Override
    public void onRosterLoaded(Roster roster) {
      loadPlayers(_positionView.getPosition(), true);
    }
}
