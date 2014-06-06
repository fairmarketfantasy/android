package com.fantasysport.factories;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.fantasysport.Const;
import com.fantasysport.activities.footballworldcup.FWCPredictionsListActivity;
import com.fantasysport.fragments.main.IMainFragment;
import com.fantasysport.fragments.main.footballwoldcup.FWCFragment;
import com.fantasysport.fragments.pages.footballwoldcup.PredictionListFragment;

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
        return FWCPredictionsListActivity.class;
    }

    @Override
    public Fragment getActivePredictionListFragment() {
        Fragment fragment = new PredictionListFragment();
        Bundle args = new Bundle();
        args.putBoolean(Const.IS_HISTORY, false);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Fragment getHistoryPredictionListFragment() {
        Fragment fragment = new PredictionListFragment();
        Bundle args = new Bundle();
        args.putBoolean(Const.IS_HISTORY, true);
        fragment.setArguments(args);
        return fragment;
    }
}
