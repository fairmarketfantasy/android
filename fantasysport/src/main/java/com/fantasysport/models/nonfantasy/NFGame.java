package com.fantasysport.models.nonfantasy;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by bylynka on 5/21/14.
 */
public class NFGame implements Serializable {

    @SerializedName("home_team")
    private NFTeam _homeTeam;

    @SerializedName("away_team")
    private NFTeam _awayTeam;

    @SerializedName("stats_id")
    private String _statsId;

    public NFGame(){
    }

    public NFGame(NFTeam home, NFTeam away, String statsId){
        _homeTeam = home;
        _awayTeam = away;
        _statsId = statsId;
    }

    public String getStatsId(){
        return _statsId;
    }

    public NFTeam getHomeTeam(){
        return _homeTeam;
    }

    public NFTeam getAwayTeam(){
        return _awayTeam;
    }

    public Date getDate(){
        return _homeTeam.getDate();
    }

}
