package com.fantasysport.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import com.fantasysport.fragments.*;
import com.fantasysport.models.Prediction;
import com.fantasysport.repo.Storage;
import com.fantasysport.webaccess.WebProxy;

/**
 * Created by bylynka on 3/14/14.
 */
public class MainActivityPagerAdapter extends FragmentPagerAdapter {

    private BaseHomeFragment _homeFragment;
    private BasePlayersFragment _playersFragment;
    private WebProxy _proxy;
    private PredictionRoster _predictionRoster;
    private MainFragmentMediator _fragmentMediator;

    public MainActivityPagerAdapter(PredictionRoster predictionRoster, FragmentManager fragmentManager, WebProxy proxy) {
        super(fragmentManager);
        _proxy = proxy;
        _predictionRoster = predictionRoster;
        _fragmentMediator = new MainFragmentMediator();
    }

    @Override
    public Fragment getItem(int i) {
        return i == 0 ? getHomeFragment() : getPlayersFragment();
    }

    private BaseHomeFragment getHomeFragment() {
        if (_homeFragment == null) {
            switch (_predictionRoster) {
                case Active:
                    _homeFragment = new HomeActivePredictionFragment(_proxy, _fragmentMediator);
                    break;
                case History:
                    _homeFragment = new HomeHistoryPredictionFragment(_proxy, _fragmentMediator);
                    break;
                default:
                    _homeFragment = new HomeFragment(_proxy, _fragmentMediator);
                    break;
            }
        }
        return _homeFragment;
    }

    private BasePlayersFragment getPlayersFragment() {
        if (_playersFragment == null) {
            switch (_predictionRoster) {
                case Active:
                    _playersFragment = new PredictionActivePlayersFragment(_proxy, _fragmentMediator);
                    break;
                case History:
                    _playersFragment = new PlayersHistoryPredictionFragment(_proxy, _fragmentMediator);
                    break;
                default:
                    _playersFragment = new PlayersFragment(_proxy, _fragmentMediator);
            }
        }
        return _playersFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

}
