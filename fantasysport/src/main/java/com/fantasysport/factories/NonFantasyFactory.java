package com.fantasysport.factories;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.fantasysport.Const;
import com.fantasysport.activities.nonfantasy.NFPredictionsListActivity;
import com.fantasysport.fragments.main.IMainFragment;
import com.fantasysport.fragments.main.NFPredictionFragment;
import com.fantasysport.fragments.main.NonFantasyFragment;
import com.fantasysport.fragments.pages.nonfantasy.NFPredictionListFragment;

/**
 * Created by bylynka on 5/26/14.
 */
public class NonFantasyFactory implements ISportFactory {

    private static NonFantasyFactory _instance;

    private NonFantasyFactory(){}

    public static NonFantasyFactory instance() {
        if (_instance == null) {
            synchronized (NonFantasyFactory.class) {
                if (_instance == null) {
                    _instance = new NonFantasyFactory();
                }
            }
        }
        return _instance;
    }

    @Override
    public IMainFragment getMainFragment() {
        return new NonFantasyFragment();
    }

    @Override
    public IMainFragment getPredictionFragment() {
        return new NFPredictionFragment();
    }

    @Override
    public Class<?> getPredictionListActivity() {
        return NFPredictionsListActivity.class;
    }

    @Override
    public Fragment getActivePredictionListFragment() {
        Fragment fragment = new NFPredictionListFragment();
        Bundle args = new Bundle();
        args.putBoolean(Const.IS_HISTORY, false);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Fragment getHistoryPredictionListFragment() {
        Fragment fragment = new NFPredictionListFragment();
        Bundle args = new Bundle();
        args.putBoolean(Const.IS_HISTORY, true);
        fragment.setArguments(args);
        return fragment;
    }
}