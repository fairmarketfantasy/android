package com.fantasysport.factories;

import android.support.v4.app.Fragment;
import com.fantasysport.fragments.main.IMainFragment;
import com.fantasysport.fragments.main.footballwoldcup.FWCFragment;

/**
 * Created by bylynka on 6/3/14.
 */
public class FWCFactory implements ISportFactory  {

    private static FWCFactory _instance;

    private FWCFactory(){}

    public static FWCFactory instance() {
        if (_instance == null) {
            synchronized (FWCFactory.class) {
                if (_instance == null) {
                    _instance = new FWCFactory();
                }
            }
        }
        return _instance;
    }

    @Override
    public IMainFragment getMainFragment() {
        return new FWCFragment();
    }

    @Override
    public IMainFragment getPredictionFragment() {
        return null;
    }

    @Override
    public Class<?> getPredictionListActivity() {
        return null;
    }

    @Override
    public Fragment getActivePredictionListFragment() {
        return null;
    }

    @Override
    public Fragment getHistoryPredictionListFragment() {
        return null;
    }
}
