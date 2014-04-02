package com.fantasysport.fragments;

import android.view.View;
import com.fantasysport.models.Market;
import com.fantasysport.models.Roster;
import com.fantasysport.webaccess.WebProxy;

/**
 * Created by bylynka on 3/24/14.
 */
public class PredictionActivePlayersFragment extends BasePlayersFragment {

    @Override
    protected void loadPlayers(final String position){
        Market market = getMarket();
        if(market == _lastMarket && _lastPosition == position
                && _lastBenchedState == canBenched() || getRoster() == null){
            return;
        }
        showProgress();
        _lastMarket = market;
        _lastPosition = position;
        _lastBenchedState = canBenched();
        getWebProxy().getPlayers(position, canBenched(), getRoster().getId(), _playersListener);
    }

    @Override
    public void onRosterLoaded(Roster roster) {
      loadPlayers(_positionView.getPosition());
    }

    @Override
    public void onUpdated(Object initiator) {

    }

    @Override
    public void onRefreshStarted(View view) {

    }
}
