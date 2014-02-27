package com.fantasysport.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
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

    public List<Market> getMarkets(){
        return _markets;
    }

    @Override
    public Fragment getItem(int i) {
        return new GameItemFragment(_markets.get(i));
    }

    @Override
    public int getCount() {
        return _markets != null?_markets.size(): 0;
    }
}
