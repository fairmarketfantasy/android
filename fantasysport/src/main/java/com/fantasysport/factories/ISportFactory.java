package com.fantasysport.factories;

import android.app.Activity;
import android.support.v4.app.Fragment;
import com.fantasysport.fragments.main.IMainFragment;

/**
 * Created by bylynka on 5/26/14.
 */
public interface ISportFactory {

    IMainFragment getMainFragment();
    Class<?> getPredictionListActivity();
    Fragment getActivePredictionListFragment();
    Fragment getHistoryPredictionListFragment();


}
