package com.fantasysport.models.fwc;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bylynka on 6/2/14.
 */
public class FWCData {

    @SerializedName("daily_wins")
    private List<Game> _games;

    @SerializedName("win_the_cup")
    private List<Team> _teams;

    @SerializedName("win_groups")
    private List<Group> _groups;

    @SerializedName("mvp")
    private List<Player> _players;

    public List<Game> getGames(){
        return _games;
    }

    public List<Team> getTeams(){
        return _teams;
    }

    public List<Group> getGroups(){
        return _groups;
    }

    public List<Player> getPlayers(){
        return _players;
    }


    public void setGames(List<Game> games) {
        this._games = games;
    }

    public void setPlayers(List<Player> players) {
        this._players = players;
    }

    public void setTeams(List<Team> teams) {
        this._teams = teams;
    }
}
