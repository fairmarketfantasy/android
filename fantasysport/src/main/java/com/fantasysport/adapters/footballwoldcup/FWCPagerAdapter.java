package com.fantasysport.adapters.footballwoldcup;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.fantasysport.fragments.pages.nonfantasy.footballwoldcup.GamesFragment;
import com.fantasysport.fragments.pages.nonfantasy.footballwoldcup.GroupsFragment;
import com.fantasysport.fragments.pages.nonfantasy.footballwoldcup.PlayersFragment;
import com.fantasysport.fragments.pages.nonfantasy.footballwoldcup.TeamsFragment;
import com.fantasysport.models.fwc.FWCData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 6/3/14.
 */
public class FWCPagerAdapter extends FragmentStatePagerAdapter {

    private List<CategoryType> _catTypes;

    public FWCPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFWCData(FWCData data){
        _catTypes = new ArrayList<CategoryType>();
        if(data.getGames() != null){
            _catTypes.add(CategoryType.Games);
        }
        if( data.getTeams() != null){
           _catTypes.add(CategoryType.Teams);
        }
        if(data.getGroups() != null){
            _catTypes.add(CategoryType.Groups);
        }

        if(data.getPlayers() != null){
            _catTypes.add(CategoryType.Players);
        }
    }

    @Override
    public Fragment getItem(int i) {
         _catTypes.get(i);
        switch (_catTypes.get(i)){
            case Games:
                return getGameFragment();
            case Groups:
                return getGroupFragment();
            case Players:
                return getPlayersFragment();
            default:
                return getTeamsFragment();
        }
    }

    @Override
    public int getCount() {
       return _catTypes == null? 0 : _catTypes.size();
    }

    private Fragment getPlayersFragment(){
        return new PlayersFragment();
    }

    private Fragment getGameFragment(){
        return new GamesFragment();
    }

    private Fragment getGroupFragment(){
        return new GroupsFragment();
    }

    private Fragment getTeamsFragment(){
        return new TeamsFragment();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    enum CategoryType{
        Teams,
        Groups,
        Games,
        Players
    }
}
