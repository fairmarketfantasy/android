package com.fantasysport.factories;

import android.support.v4.app.Fragment;
import com.fantasysport.activities.fantasy.FPredictionsListActivity;
import com.fantasysport.fragments.main.FantasyFragment;
import com.fantasysport.fragments.main.FantasyPredictionFragment;
import com.fantasysport.fragments.main.IMainFragment;
import com.fantasysport.fragments.pages.fantasy.ActivePredictionListFragment;
import com.fantasysport.fragments.pages.fantasy.HistoryPredictionListFragment;

/**
 * Created by bylynka on 5/26/14.
 */
public class FantasyFactory implements ISportFactory {


    private static FantasyFactory _instance;

    private FantasyFactory(){}

    public static FantasyFactory instance() {
        if (_instance == null) {
            synchronized (FantasyFactory.class) {
                if (_instance == null) {
                    _instance = new FantasyFactory();
                }
            }
        }
        return _instance;
    }


    @Override
    public IMainFragment getMainFragment() {
        return new FantasyFragment();
    }

    @Override
    public IMainFragment getPredictionFragment() {
        return new FantasyPredictionFragment();
    }

    @Override
    public Class<?> getPredictionListActivity() {
        return FPredictionsListActivity.class;
    }

    @Override
    public Fragment getActivePredictionListFragment() {
        return new ActivePredictionListFragment();
    }

    @Override
    public Fragment getHistoryPredictionListFragment() {
        return new HistoryPredictionListFragment();
    }
}
