package com.fantasysport.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.adapters.MainActivityPagerAdapter;
import com.fantasysport.fragments.MainFragmentMediator;
import com.fantasysport.fragments.PredictionRoster;
import com.fantasysport.models.Market;
import com.fantasysport.models.Roster;
import com.fantasysport.views.AnimatedViewPager;
import com.fantasysport.views.animations.ZoomOutPageTransformer;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.requestListeners.RosterResponseListener;
import com.fantasysport.webaccess.requestListeners.UpdateMainDataResponse;
import com.fantasysport.webaccess.requestListeners.UpdateMainDataResponseListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 3/24/14.
 */
public class BaseMainActivity extends BaseActivity {

    protected MainActivityPagerAdapter _mainActivityPagerAdapter;
    protected Market _market;
    protected Roster _roster;
    protected List<IListener> _listeners = new ArrayList<IListener>();
    protected boolean _removeBenchedPlayers = true;
    protected ImageView _leftSwipeImg;
    protected ImageView _rightSwipeImg;
    protected AnimatedViewPager _pager;
    protected List<Market> _markets;
    protected List<IRosterLoadedListener> _rosterLoadedListeners = new ArrayList<IRosterLoadedListener>();
    protected List<IUpdateListener> _updateListener = new ArrayList<IUpdateListener>();
    protected PredictionRoster _predictionRoster = PredictionRoster.None;
    protected MainFragmentMediator _fragmentMediator = new MainFragmentMediator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initStartParams(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        _leftSwipeImg = getViewById(R.id.left_point_img);
        _rightSwipeImg = getViewById(R.id.right_point_img);
        setPager();
        setPageIndicator(0);
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

    protected void initStartParams(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            _removeBenchedPlayers = savedInstanceState.getBoolean(Const.REMOVE_BENCHED_PLAYERS, true);
            _market = (Market) savedInstanceState.getSerializable(Const.MARKET);
            _roster = (Roster) savedInstanceState.getSerializable(Const.ROSTER);
            _markets = (ArrayList<Market>) savedInstanceState.getSerializable(Const.MARKETS);
        } else {
            _markets = _storage.getMarkets();
        }
    }

    public boolean canRemoveBenchedPlayers() {
        return _removeBenchedPlayers;
    }

    public void setCanBenchedPlayers(boolean canBenched) {
        _removeBenchedPlayers = canBenched;
    }

    public void addListener(IListener listener) {
        _listeners.add(listener);
    }

    protected void raiseOnUpdateListener(Object initiator) {
        for (int i = 0; i < _listeners.size(); i++) {
            _updateListener.get(i).onUpdated(initiator);
        }
    }

    protected void raiseOnPageChanged(int page) {
        for (int i = 0; i < _listeners.size(); i++) {
            _listeners.get(i).onPageChanged(page);
        }
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

    protected void setPager() {
        _mainActivityPagerAdapter = new MainActivityPagerAdapter(PredictionRoster.None, getSupportFragmentManager());
        _pager = getViewById(R.id.root_pager);
        _pager.setPageTransformer(true, new ZoomOutPageTransformer());
        _pager.setAdapter(_mainActivityPagerAdapter);
        _pager.setOnPageChangeListener(new AnimatedViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPageIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    protected void setPageIndicator(int position) {
        Drawable drawable = position == 0 ? getResources().getDrawable(R.drawable.swipe_active) : getResources().getDrawable(R.drawable.swipe_passive);
        _leftSwipeImg.setBackgroundDrawable(drawable);
        drawable = position != 0 ? getResources().getDrawable(R.drawable.swipe_active) : getResources().getDrawable(R.drawable.swipe_passive);
        _rightSwipeImg.setBackgroundDrawable(drawable);
        raiseOnPageChanged(position);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
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

    @Override
    public void onBackPressed() {
        setResult(Const.FINISH_ACTIVITY);
        super.onBackPressed();
    }

    public void navigateToPlayers() {
        _pager.setCurrentItem(1, false);
    }

    public void navigateToRosters() {
        _pager.setCurrentItem(0, false);
    }

    public List<Market> getMarkets() {
        return _markets;
    }

    public interface IListener {
        public void onPageChanged(int page);
    }

    protected void raiseOnRosterLoaded(Roster roster){
        for (IRosterLoadedListener listener : _rosterLoadedListeners){
            listener.onRosterLoaded(roster);
        }
    }

    public void loadRoster(int rosterId){
        showProgress();
        _webProxy.getRoster(rosterId, new RosterResponseListener() {
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
        _webProxy.getMainData(_storage.getUserData().getId(), new UpdateMainDataResponseListener() {
            @Override
            public void onRequestError(RequestError error) {
                raiseOnUpdateListener(initiator);
                showAlert(getString(R.string.error), error.getMessage());
            }

            @Override
            public void onRequestSuccess(UpdateMainDataResponse response) {
                raiseOnUpdateListener(initiator);
                Roster updatedRoster = response.getRoster();
                if (_roster != null && updatedRoster != null && _roster.getId() == updatedRoster.getId()){
                    _roster = updatedRoster;
                    raiseOnRosterLoaded(updatedRoster);
                }
            }
        });
    }

    public interface IRosterLoadedListener {
        public void onRosterLoaded(Roster roster);
    }

    public interface IUpdateListener {
        public void onUpdated(Object initiator);
    }

}