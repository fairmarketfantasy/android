package com.fantasysport.fragments.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.adapters.MainActivityPagerAdapter;
import com.fantasysport.fragments.BaseActivityFragment;
import com.fantasysport.fragments.MainFragmentMediator;
import com.fantasysport.fragments.PredictionRoster;
import com.fantasysport.models.Market;
import com.fantasysport.models.Roster;
import com.fantasysport.views.AnimatedViewPager;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.requestListeners.RosterResponseListener;
import com.fantasysport.webaccess.requestListeners.UpdateMainDataResponse;
import com.fantasysport.webaccess.requestListeners.UpdateMainDataResponseListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 5/13/14.
 */
public class BaseFantasyFragment extends BaseActivityFragment
        implements IMainFragment {

    protected MainActivityPagerAdapter _mainActivityPagerAdapter;
    protected Market _market;
    protected Roster _roster;
    protected List<IPageChangedListener> _listeners = new ArrayList<IPageChangedListener>();
    protected boolean _removeBenchedPlayers = true;
    protected List<Market> _markets;
    protected List<IRosterLoadedListener> _rosterLoadedListeners = new ArrayList<IRosterLoadedListener>();
    protected List<IUpdateListener> _updateListener = new ArrayList<IUpdateListener>();
    protected PredictionRoster _predictionRoster = PredictionRoster.None;
    protected MainFragmentMediator _fragmentMediator = new MainFragmentMediator();
    protected AnimatedViewPager _pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_root_main, container, false);
        initStartParams(savedInstanceState);
        setPager();
        return _rootView;
    }

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

    protected void raiseOnPageChanged(int page) {
        for (int i = 0; i < _listeners.size(); i++) {
            _listeners.get(i).onPageChanged(page);
        }
    }

    public void setRoster(Roster roster) {
        _roster = roster;
    }

    public boolean canRemoveBenchedPlayers() {
        return _removeBenchedPlayers;
    }

    public void addPageChangedListener(IPageChangedListener listener) {
        _listeners.add(listener);
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
        for (int i = 0; i < _listeners.size(); i++) {
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

    protected void setPager() {
        _mainActivityPagerAdapter = new MainActivityPagerAdapter(PredictionRoster.None, getActivity().getSupportFragmentManager());
        _pager = getViewById(R.id.root_pager);
        _pager.setAdapter(_mainActivityPagerAdapter);
        _pager.setOnPageChangeListener(new AnimatedViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                raiseOnPageChanged(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
