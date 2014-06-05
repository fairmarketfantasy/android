package com.fantasysport.fragments.main.fantasy;

import android.os.Bundle;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.adapters.fantasy.FantasyPagerAdapter;
import com.fantasysport.fragments.main.BaseFragment;
import com.fantasysport.fragments.pages.fantasy.MainFragmentMediator;
import com.fantasysport.fragments.pages.fantasy.PredictionRoster;
import com.fantasysport.models.Market;
import com.fantasysport.models.Roster;
import com.fantasysport.webaccess.responseListeners.RequestError;
import com.fantasysport.webaccess.responseListeners.RosterResponseListener;
import com.fantasysport.webaccess.responseListeners.UpdateMainDataResponse;
import com.fantasysport.webaccess.responseListeners.UpdateMainDataResponseListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 5/13/14.
 */
public abstract class BaseFantasyFragment extends BaseFragment {

    protected FantasyPagerAdapter _mainActivityPagerAdapter;
    protected Market _market;
    protected Roster _roster;
    protected boolean _removeBenchedPlayers = true;
    protected List<Market> _markets;
    protected List<IRosterLoadedListener> _rosterLoadedListeners = new ArrayList<IRosterLoadedListener>();
    protected List<IUpdateListener> _updateListener = new ArrayList<IUpdateListener>();
    protected PredictionRoster _predictionRoster = PredictionRoster.None;
    protected MainFragmentMediator _fragmentMediator = new MainFragmentMediator();

    @Override
    protected void initStartParams(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            _removeBenchedPlayers = savedInstanceState.getBoolean(Const.REMOVE_BENCHED_PLAYERS, true);
            _market = (Market) savedInstanceState.getSerializable(Const.MARKET);
            _roster = (Roster) savedInstanceState.getSerializable(Const.ROSTER);
            _markets = (ArrayList<Market>) savedInstanceState.getSerializable(Const.MARKETS);
        } else {
            _markets = getStorage().getMarkets();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Const.REMOVE_BENCHED_PLAYERS, _removeBenchedPlayers);
        if (_markets != null) {
            outState.putSerializable(Const.MARKETS, new ArrayList<Market>(_markets));
        }
        if (_market == null) {
            return;
        }
        outState.putSerializable(Const.MARKET, _market);
        if (_roster != null) {
            outState.putSerializable(Const.ROSTER, _roster);
        }
    }

    public void setCanBenchedPlayers(boolean canBenched) {
        _removeBenchedPlayers = canBenched;
    }

    public Market getMarket() {
        return _market;
    }

    public void setMarket(Market market) {
        _market = market;
    }

    public Roster getRoster() {
        return _roster;
    }

    public void setRoster(Roster roster) {
        _roster = roster;
    }

    public boolean canRemoveBenchedPlayers() {
        return _removeBenchedPlayers;
    }

    public MainFragmentMediator getFragmentMediator(){
        return _fragmentMediator;
    }

    public void addRosterLoadedListener(IRosterLoadedListener listener){
        _rosterLoadedListeners.add(listener);
    }

    public void addUpdateListener(IUpdateListener listener){
        _updateListener.add(listener);
    }

    public PredictionRoster getPredictionRoster(){
        return _predictionRoster;
    }


    protected void raiseOnUpdateListener(Object initiator) {
        for (int i = 0; i < _updateListener.size(); i++) {
            _updateListener.get(i).onUpdated(initiator);
        }
    }

    protected void raiseOnRosterLoaded(Roster roster){
        for (IRosterLoadedListener listener : _rosterLoadedListeners){
            listener.onRosterLoaded(roster);
        }
    }

    public List<Market> getMarkets() {
        return _markets;
    }

    public void loadRoster(int rosterId){
        showProgress();
        getWebProxy().getRoster(rosterId, new RosterResponseListener() {
            @Override
            public void onRequestError(RequestError error) {
                dismissProgress();
                showAlert(getString(R.string.error), error.getMessage());
            }

            @Override
            public void onRequestSuccess(Roster roster) {
                dismissProgress();
                _roster = roster;
                raiseOnRosterLoaded(roster);
            }
        });
    }

    public void updateUserData(final Object initiator){
        getWebProxy().getMainData(getStorage().getUserData().getId(), new UpdateMainDataResponseListener() {
            @Override
            public void onRequestError(RequestError error) {
                raiseOnUpdateListener(initiator);
                showAlert(getString(R.string.error), error.getMessage());
            }

            @Override
            public void onRequestSuccess(UpdateMainDataResponse response) {
                raiseOnUpdateListener(initiator);
                Roster updatedRoster = response.getRoster();
                if (_roster != null && updatedRoster != null && _roster.getId() == updatedRoster.getId()) {
                    _roster = updatedRoster;
                    raiseOnRosterLoaded(updatedRoster);
                }
            }
        });
    }

    @Override
    protected void setPager() {
        super.setPager();
        _mainActivityPagerAdapter = new FantasyPagerAdapter(PredictionRoster.None, getActivity().getSupportFragmentManager());
        _pager.setAdapter(_mainActivityPagerAdapter);
        setPageAmount(_mainActivityPagerAdapter.getCount());
        raiseOnPageChanged(0);
    }

    public void navigateToPlayers() {
        _pager.setCurrentItem(1, false);
    }

    public void navigateToRosters() {
        _pager.setCurrentItem(0, false);
    }

    public interface IRosterLoadedListener {
        public void onRosterLoaded(Roster roster);
    }

    public interface IUpdateListener {
        public void onUpdated(Object initiator);
    }
}
