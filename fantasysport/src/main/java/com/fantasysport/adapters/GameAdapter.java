package com.fantasysport.adapters;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.View;
import com.fantasysport.Const;
import com.fantasysport.fragments.GameItemFragment;
import com.fantasysport.models.Market;

import java.util.List;

/**
 * Created by bylynka on 2/24/14.
 */
public class GameAdapter extends FragmentStatePagerAdapter {

    private List<Market> _markets;

    public GameAdapter(FragmentManager fm, List<Market> markets) {
        super(fm);
        _markets = markets;
    }

    public int getMarketPosition(Market market) {
        if (_markets == null || market == null) {
            return -1;
        }
        return _markets.indexOf(market);
    }

    public List<Market> getMarkets() {
        return _markets;
    }

    @Override
    public Fragment getItem(int i) {
        GameItemFragment fragment = new GameItemFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Const.MARKET, _markets.get(i));
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public int getCount() {
        Log.i("count!!!!!!!!!!!!!", "called");
        return _markets != null ? _markets.size() : 0;
    }

    public void setMarkets(List<Market> markets) {
        _markets = markets;
    }

    @Override
    public int getItemPosition(Object object) {
        int position;
        if (object == null || _markets == null || !(object instanceof GameItemFragment)) {
            position = -1;
        } else {
            Market market = ((GameItemFragment) object).getMarket();
            position = _markets.indexOf(market);
        }
        if (position >= 0) {
            return position;
        } else {
            return POSITION_NONE;
        }
    }
}
