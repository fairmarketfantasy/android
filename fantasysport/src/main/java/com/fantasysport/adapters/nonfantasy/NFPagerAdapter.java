package com.fantasysport.adapters.nonfantasy;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.fantasysport.fragments.pages.nonfantasy.GameCandidatesFragment;
import com.fantasysport.fragments.pages.nonfantasy.GameRosterFragment;
import com.fantasysport.fragments.pages.nonfantasy.PredictionGameRosterFragment;
import com.fantasysport.models.fwc.Game;

/**
 * Created by bylynka on 5/16/14.
 */
public class NFPagerAdapter extends FragmentStatePagerAdapter {

    private final int PAGE_COUNT = 2;

    private GameCandidatesFragment _gameCandidatesFragment;
    private GameRosterFragment _gameRosterFragment;
    private PredictionGameRosterFragment _predGameRosterFragment;
    private boolean _isPrediction = false;

    public NFPagerAdapter(boolean isPrediction, FragmentManager fm) {
        super(fm);
        _isPrediction = isPrediction;
    }

    @Override
    public Fragment getItem(int i) {
        return i == 0
                ? (_isPrediction? getPredGameRosterFragment() :getGameRosterFragment())
                : getGameCandidatesFragment();
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    private PredictionGameRosterFragment getPredGameRosterFragment(){
        if(_predGameRosterFragment == null){
            _predGameRosterFragment = new PredictionGameRosterFragment();
        }
        return _predGameRosterFragment;
    }

    private GameRosterFragment getGameRosterFragment(){
        if(_gameRosterFragment == null){
            _gameRosterFragment = new GameRosterFragment();
        }
        return _gameRosterFragment;
    }

    private GameCandidatesFragment getGameCandidatesFragment(){
        if(_gameCandidatesFragment == null){
            _gameCandidatesFragment = new GameCandidatesFragment();
        }
        return _gameCandidatesFragment;
    }
}
