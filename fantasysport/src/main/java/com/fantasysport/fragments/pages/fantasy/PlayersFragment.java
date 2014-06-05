package com.fantasysport.fragments.pages.fantasy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fantasysport.R;
import com.fantasysport.fragments.main.fantasy.FantasyFragment;
import com.fantasysport.models.Market;
import com.fantasysport.models.fantasy.Position;
import com.fantasysport.models.Roster;
import com.fantasysport.webaccess.responseListeners.RequestError;
import com.fantasysport.webaccess.responseListeners.RosterResponseListener;

import java.util.List;

/**
 * Created by bylynka on 3/14/14.
 */
public class PlayersFragment extends BasePlayersFragment implements FantasyFragment.IOnMarketsListener{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((FantasyFragment)getBaseFFragment()).addOnMarketsListener(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void loadPlayers(final Position position){
        Market market = getMarket();
        if(position == null || market == _lastMarket && _lastPosition == position.getAcronym()
                && _lastBenchedState == canBenched() && !_pullToRefreshLayout.isRefreshing() && getRoster() != null
                || market == null || _playersAdapter == null){
            return;
        }
        setPositionLabel(position.getName());
        showProgress();
        _playersAdapter.setItems(null);
        _playersAdapter.notifyDataSetInvalidated();
        _lastMarket = market;
        _lastPosition = position.getAcronym();
        _lastBenchedState = canBenched();
        if (getRoster() == null) {
            getWebProxy().createRoster(market.getId(), new RosterResponseListener() {
                @Override
                public void onRequestError(RequestError error) {
                    dismissProgress();
                    showAlert(getString(R.string.error), error.getMessage());
                }
                @Override
                public void onRequestSuccess(Roster roster) {
                    setRoster(roster);
                    getWebProxy().getPlayers(position.getAcronym(), canBenched(), getRoster().getId(), _playersListener);
                }
            });
        } else {
            getWebProxy().getPlayers(position.getAcronym(), canBenched(), getRoster().getId(), _playersListener);
        }
    }

    @Override
    public void onRosterLoaded(Roster roster) {

    }

    @Override
    public void onPageChanged(int page) {
        Roster roster = getRoster();
        if(page != 1 || roster != null){
            return;
        }
        loadPlayers(_positionView.getPosition());
    }

    @Override
    public void onMarkets(List<Market> markets) {
        updateMarkets();
        _positionView.setPositions(getStorage().getDefaultRosterData().getPositions());
        if(markets == null || markets.size() == 0){
            _playersAdapter.setItems(null);
            _playersAdapter.notifyDataSetInvalidated();
            return;
        }
        _pager.setCurrentItem(0 , false);
        getFragmentMediator().changeMarket(this, markets.get(0));
    }
}
