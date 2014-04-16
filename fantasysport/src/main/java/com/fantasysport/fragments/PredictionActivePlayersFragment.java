package com.fantasysport.fragments;

import android.view.View;
import com.fantasysport.models.Market;
import com.fantasysport.models.Position;
import com.fantasysport.models.Roster;
import com.fantasysport.webaccess.WebProxy;

/**
 * Created by bylynka on 3/24/14.
 */
public class PredictionActivePlayersFragment extends BasePlayersFragment {

    @Override
    protected void loadPlayers(final Position position){
        Market market = getMarket();
        if(market == _lastMarket && _lastPosition == position.getAcronym()
                && _lastBenchedState == canBenched() || getRoster() == null){
            return;
        }
        setPositionLabel(position.getName());
        showProgress();
        _lastMarket = market;
        _lastPosition = position.getAcronym();
        _lastBenchedState = canBenched();
        getWebProxy().getPlayers(position.getAcronym(), canBenched(), getRoster().getId(), _playersListener);
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
