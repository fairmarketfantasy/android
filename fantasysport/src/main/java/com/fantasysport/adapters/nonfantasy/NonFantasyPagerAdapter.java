package com.fantasysport.adapters.nonfantasy;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.fantasysport.fragments.pages.nonfantasy.GameCandidatesFragment;
import com.fantasysport.fragments.pages.nonfantasy.GameRosterFragment;

/**
 * Created by bylynka on 5/16/14.
 */
public class NonFantasyPagerAdapter extends FragmentPagerAdapter {

    private final int PAGE_COUNT = 2;

    private GameCandidatesFragment _gameCandidatesFragment;
    private GameRosterFragment _gameRosterFragment;

    public NonFantasyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return i == 0 ? getGameRosterFragment() : getGameCandidatesFragment();
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
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
