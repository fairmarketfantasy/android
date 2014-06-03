package com.fantasysport.factories;

import com.fantasysport.Const;

/**
 * Created by bylynka on 5/26/14.
 */
public class FactoryProvider {

    public static ISportFactory getFactory(int sportType){
        switch (sportType){
            case Const.FANTASY_SPORT:
                return FantasyFactory.instance();
            case Const.NON_FANTASY_SPORT:
                return NonFantasyFactory.instance();
            case Const.FWC:
                return FWCFactory.instance();
        }
        return null;
    }

}
