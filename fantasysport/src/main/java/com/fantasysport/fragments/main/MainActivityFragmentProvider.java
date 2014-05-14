package com.fantasysport.fragments.main;

import com.fantasysport.Const;

/**
 * Created by bylynka on 5/13/14.
 */
public class MainActivityFragmentProvider {

    public static IMainFragment getFragment(int fragmentCode){

        switch (fragmentCode){
            case Const.FANTASY_SPORT:
                return new FantasyFragment();
        }
        return null;
    }
}
