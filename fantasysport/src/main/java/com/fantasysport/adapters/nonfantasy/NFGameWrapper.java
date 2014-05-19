package com.fantasysport.adapters.nonfantasy;

import com.fantasysport.models.NFGame;

/**
 * Created by bylynka on 5/19/14.
 */
public class NFGameWrapper implements INFGame {

    private NFGame _game;
    private SelectedTeamType _selectedTeam;

    public NFGameWrapper(NFGame game, SelectedTeamType selectedTeam){
        _game = game;
        _selectedTeam = selectedTeam;
    }

    public NFGame getGame(){
        return _game;
    }

    public SelectedTeamType getSelectedTeamType(){
        return _selectedTeam;
    }

    public enum SelectedTeamType{
        Home,
        Away
    }
}
