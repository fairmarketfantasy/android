package com.fantasysport.adapters.fantasy;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.fantasysport.fragments.pages.fantasy.*;

/**
 * Created by bylynka on 3/14/14.
 */
public class FantasyPagerAdapter extends FragmentStatePagerAdapter {

    private BaseHomeFragment _homeFragment;
    private BasePlayersFragment _playersFragment;
    private PredictionRoster _predictionRoster;


    public FantasyPagerAdapter(PredictionRoster predictionRoster, FragmentManager fragmentManager) {
        super(fragmentManager);
        _predictionRoster = predictionRoster;
    }

    @Override
    public Fragment getItem(int i) {
        return i == 0 ? getHomeFragment() : getPlayersFragment();
    }

    private BaseHomeFragment getHomeFragment() {
        if (_homeFragment == null) {
            switch (_predictionRoster) {
                case Active:
                    _homeFragment = new HomeActivePredictionFragment();
                    break;
                case History:
                    _homeFragment = new HomeHistoryPredictionFragment();
                    break;
                default:
                    _homeFragment = new HomeFragment();
                    break;
            }
        }
        return _homeFragment;
    }

    private BasePlayersFragment getPlayersFragment() {
        if (_playersFragment == null) {
            switch (_predictionRoster) {
                case Active:
                    _playersFragment = new PredictionActivePlayersFragment();
                    break;
                case History:
                    _playersFragment = new PlayersHistoryPredictionFragment();
                    break;
                default:
                    _playersFragment = new PlayersFragment();
            }
        }
        return _playersFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

}
