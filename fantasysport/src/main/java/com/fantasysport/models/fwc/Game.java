package com.fantasysport.models.fwc;

import com.fantasysport.utility.Converter;
import com.fantasysport.utility.DateUtils;
import com.fantasysport.utility.DeviceInfo;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by bylynka on 6/2/14.
 */
public class Game {

    @SerializedName("stats_id")
    private String _statsId;

    @SerializedName("game_time")
    private String _gameTime;

    @SerializedName("get_home_team")
    private Team _homeTeam;

    @SerializedName("get_away_team")
    private Team _awayTeam;

    public Date getGameDate() {
        Date date = Converter.toDate(_gameTime);
        int gmtInMinutes = DeviceInfo.getGMTInMinutes();
        return DateUtils.addMinutes(date, gmtInMinutes);
    }

    public Team getHomeTeam(){
        return _homeTeam;
    }

    public Team getAwayTeam(){
        return _awayTeam;
    }

    public String getStatsId(){
        return _statsId;
    }
}
