package com.fantasysport.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bylynka on 5/16/14.
 */
public class NFDataContainer {

    @SerializedName("game_roster")
    private NFRoster _roster;

    @SerializedName("games")
    private List<NFGame> _candidateGames;

    public NFRoster getRoster(){
        return _roster;
    }

    public List<NFGame> getCandidateGames(){
        return _candidateGames;
    }
}
