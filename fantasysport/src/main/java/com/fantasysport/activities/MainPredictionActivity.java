package com.fantasysport.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.adapters.MainActivityPagerAdapter;
import com.fantasysport.fragments.PredictionRoster;
import com.fantasysport.models.Market;
import com.fantasysport.views.AnimatedViewPager;
import com.fantasysport.views.animations.ZoomOutPageTransformer;

import java.util.ArrayList;

/**
 * Created by bylynka on 3/24/14.
 */
public class MainPredictionActivity  extends BaseMainActivity {

    private int _rosterId;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_prediction_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setPager() {
        _mainActivityPagerAdapter = new MainActivityPagerAdapter( _predictionRoster, getSupportFragmentManager(), _webProxy);
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
        if(_rosterId > 0){
            loadRoster(_rosterId);
        }
    }


    @Override
    protected void initStartParams(Bundle savedInstanceState) {
        super.initStartParams(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            _predictionRoster = intent.hasExtra(Const.PREDICTION) ? (PredictionRoster) intent.getSerializableExtra(Const.PREDICTION) : PredictionRoster.None;
            Market market = (Market)intent.getSerializableExtra(Const.MARKET);
            _markets = new ArrayList<Market>();
            _markets.add(market);
            _rosterId = intent.getIntExtra(Const.ROSTER_ID, -1);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Const.PREDICTION, _predictionRoster);
    }


    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        switch (item.getItemId()) {
            case R.id.arrow_close:
                item.setVisible(false);
                _menu.findItem(R.id.arrow_open).setVisible(true);
                raiseOnToggleHeader();
                return true;
            case R.id.arrow_open:
                item.setVisible(false);
                _menu.findItem(R.id.arrow_close).setVisible(true);
                raiseOnToggleHeader();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
