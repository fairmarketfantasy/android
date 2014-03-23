package com.fantasysport.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import com.fantasysport.fragments.HomeFragment;
import com.fantasysport.fragments.PlayersFragment;
import com.fantasysport.repo.Storage;
import com.fantasysport.webaccess.WebProxy;

/**
 * Created by bylynka on 3/14/14.
 */
public class MainActivityPagerAdapter extends FragmentPagerAdapter {

    private HomeFragment _homeFragment;
    private PlayersFragment _playersFragment;
    private WebProxy _proxy;

    public MainActivityPagerAdapter(FragmentManager fragmentManager, WebProxy proxy){
        super(fragmentManager);
        _proxy = proxy;
    }

    @Override
    public Fragment getItem(int i) {
        return i == 0? getHomeFragment(): getPlayersFragment();
    }

    private HomeFragment getHomeFragment(){
        if(_homeFragment == null){
            _homeFragment = new HomeFragment(_proxy);
        }
        return _homeFragment;
    }

    private PlayersFragment getPlayersFragment(){
        if(_playersFragment == null){
            _playersFragment = new PlayersFragment(_proxy);
        }
        return _playersFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
