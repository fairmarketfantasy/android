package com.fantasysport.models.fwc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by bylynka on 6/2/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FWCData {

    @JsonProperty("daily_wins")
    private List<Game> _games;

    @JsonProperty("win_the_cup")
    private List<Team> _teams;

    @JsonProperty("win_groups")
    private List<Group> _groups;

    @JsonProperty("mvp")
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
