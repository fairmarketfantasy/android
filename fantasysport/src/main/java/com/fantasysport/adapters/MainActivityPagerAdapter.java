package com.fantasysport.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import com.fantasysport.fragments.HomeFragment;
import com.fantasysport.fragments.PlayersFragment;
import com.fantasysport.repo.Storage;

/**
 * Created by bylynka on 3/14/14.
 */
public class MainActivityPagerAdapter extends FragmentPagerAdapter {

    private HomeFragment _homeFragment;
    private PlayersFragment _playersFragment;

    public MainActivityPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int i) {
        return i == 0? getHomeFragment(): getPlayersFragment();
    }

    private HomeFragment getHomeFragment(){
        if(_homeFragment == null){
            _homeFragment = new HomeFragment();
        }
        return _homeFragment;
    }

    private PlayersFragment getPlayersFragment(){
        if(_playersFragment == null){
            _playersFragment = new PlayersFragment();
        }
        return _playersFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
