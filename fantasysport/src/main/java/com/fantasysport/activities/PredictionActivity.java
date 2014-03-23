package com.fantasysport.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.TypedValue;
import com.astuetz.PagerSlidingTabStrip;
import com.fantasysport.R;
import com.fantasysport.adapters.PredictionsDropdownAdapter;
import com.fantasysport.fragments.ActivePredictionFragment;
import com.fantasysport.fragments.HistoryPredictionFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 3/19/14.
 */
public class PredictionActivity extends BaseActivity implements ActionBar.OnNavigationListener {

    private final String TIME_TYPE = "time_type";
    private final String PREDICTION_TYPE = "prediction_type";

    private int _currentTimeType = 0;
    private int _currentPredictionType = 0;
    private List<ILoadContentListener> _loadContentListeners = new ArrayList<ILoadContentListener>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predictions);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initStartParams(savedInstanceState);
        initTabs();
        setTopDropdown();
        raiseLoadContentListener();
    }

    public TimeType getTimeType(){
        return _currentTimeType == 0? TimeType.Active: TimeType.History;
    }

    public PredictionType getPredictionType(){
        return _currentPredictionType == 0? PredictionType.Roster: PredictionType.Individual;
    }

    public void addLoadContentListener(ILoadContentListener listener){
        _loadContentListeners.add(listener);
    }

    private void initStartParams(Bundle savedInstanceState){
        if(savedInstanceState == null){
            return;
        }
        _currentTimeType = savedInstanceState.getInt(TIME_TYPE);
        _currentPredictionType = savedInstanceState.getInt(PREDICTION_TYPE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TIME_TYPE, _currentTimeType);
        outState.putInt(PREDICTION_TYPE, _currentPredictionType);
    }

    private void initTabs(){
        final ViewPager pager = getViewById(R.id.pager);
        final PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        TabsPagerAdapter adapter = new TabsPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setCurrentItem(_currentTimeType);
        tabs.setViewPager(pager);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }
            @Override
            public void onPageSelected(int i) {
                _currentTimeType = i;
                pager.setCurrentItem(i, true);
                tabs.notifyDataSetChanged();
                raiseLoadContentListener();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setTopDropdown(){
        getSupportActionBar().setCustomView(null);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        TypedValue tv = new TypedValue();
        int height = 0;
        if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true))
        {
            height = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        getSupportActionBar().setListNavigationCallbacks(new PredictionsDropdownAdapter(PredictionActivity.this, height, getProhibitionRound()), this);
        getSupportActionBar().setSelectedNavigationItem(_currentPredictionType);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        _currentPredictionType = i;
        raiseLoadContentListener();
        return true;
    }

    private void raiseLoadContentListener(){
        for (ILoadContentListener listener : _loadContentListeners){
            listener.onLoad(getTimeType(), getPredictionType());
        }
    }

    public class TabsPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "Active", "History" };
        private HistoryPredictionFragment _historyFragment;
        private ActivePredictionFragment _activityFragment;

        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private HistoryPredictionFragment getHistoryFragment(){
            if(_historyFragment == null){
                _historyFragment = new HistoryPredictionFragment(_webProxy);
            }
            return _historyFragment;
        }

        private ActivePredictionFragment getActiveFragment(){
            if(_activityFragment == null){
                _activityFragment = new ActivePredictionFragment(_webProxy);
            }
            return _activityFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0? getActiveFragment(): getHistoryFragment();
        }

    }

    public interface ILoadContentListener{
        public void onLoad(TimeType timeType, PredictionType predictionType);
    }

    public enum PredictionType{
        Roster,
        Individual
    }

    public enum TimeType{
        Active,
        History
    }
}
