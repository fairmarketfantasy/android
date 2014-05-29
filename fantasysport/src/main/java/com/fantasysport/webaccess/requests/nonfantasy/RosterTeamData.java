package com.fantasysport.webaccess.requests.nonfantasy;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 5/28/14.
 */
public class RosterTeamData {
    @SerializedName("team_stats_id")
    private int _teamStatsId;

    @SerializedName("game_stats_id")
    private int _gameStatsId;

    public int getTeamStatsId(){
        return _teamStatsId;
    }

    public int getGameStatsId(){
        return _gameStatsId;
    }
}
