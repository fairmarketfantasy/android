package com.fantasysport.fragments.main;

import android.content.Intent;
import android.os.Bundle;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.adapters.fantasy.FantasyPagerAdapter;
import com.fantasysport.fragments.pages.fantasy.PredictionRoster;
import com.fantasysport.models.Market;
import com.fantasysport.views.AnimatedViewPager;
import com.fantasysport.views.animations.ZoomOutPageTransformer;

import java.util.ArrayList;

/**
 * Created by bylynka on 5/14/14.
 */
public class FantasyPredictionFragment extends BaseFantasyFragment {

    private int _rosterId;

    protected void setPager() {
        _mainActivityPagerAdapter = new FantasyPagerAdapter( _predictionRoster, getActivity().getSupportFragmentManager());
        _pager = getViewById(R.id.root_pager);
        _pager.setPageTransformer(true, new ZoomOutPageTransformer());
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
        if(_rosterId > 0){
            loadRoster(_rosterId);
        }
    }

    @Override
    protected void initStartParams(Bundle savedInstanceState) {
        super.initStartParams(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getActivity().getIntent();
            _predictionRoster = intent.hasExtra(Const.PREDICTION) ? (PredictionRoster) intent.getSerializableExtra(Const.PREDICTION) : PredictionRoster.None;
            Market market = (Market)intent.getSerializableExtra(Const.MARKET);
            _markets = new ArrayList<Market>();
            _markets.add(market);
            _rosterId = intent.getIntExtra(Const.ROSTER_ID, -1);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Const.PREDICTION, _predictionRoster);
    }

    @Override
    public void updateMainData() {
    }
}
