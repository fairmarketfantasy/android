package com.fantasysport.webaccess.requests.nonfantasy;

import com.fantasysport.utility.Converter;
import com.fantasysport.utility.DateUtils;
import com.fantasysport.utility.DeviceInfo;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by bylynka on 5/23/14.
 */
class Game {

    @SerializedName("game_stats_id")
    private int _statsId;

    @SerializedName("game_time")
    private String _gameTime;

    @SerializedName("home_team_name")
    private String _homeTeam;

    @SerializedName("away_team_name")
    private String _awayTeam;

    @SerializedName("home_team_pt")
    private double _homeTeamPt;

    @SerializedName("away_team_pt")
    private double _awayTeamPt;

    @SerializedName("home_team_stats_id")
    private int _homeTeamStatsId;

    @SerializedName("away_team_stats_id")
    private int _awayTeamStatsId;

    @SerializedName("home_team_logo_url")
    private String _homeTeamLogo;

    @SerializedName("away_team_logo_url")
    private String _awayTeamLogo;

    @SerializedName("disable_pt_home_team")
    private boolean _isHomePredicted;

    @SerializedName("disable_pt_away_team")
    private boolean _isAwayPredicted;

    @SerializedName("disable_home_team")
    private boolean _isHomeSelected = false;

    @SerializedName("disable_away_team")
    private boolean _isAwaySelected = false;

    public int getStatsId() {
        return _statsId;
    }

    public Date getGameDate() {
        Date date = Converter.toDate(_gameTime);
        int gmtInMinutes = DeviceInfo.getGMTInMinutes();
        return DateUtils.addMinutes(date, gmtInMinutes);
    }

    public boolean isHomeSelected(){
        return _isHomeSelected;
    }

    public boolean isAwaySelected(){
        return _isAwaySelected;
    }

    public String getHomeTeamName() {
        return _homeTeam;
    }

    public String getAwayTeamName() {
        return _awayTeam;
    }

    public double getHomeTeamPt() {
        return _homeTeamPt;
    }

    public double getAwayTeamPt() {
        return _awayTeamPt;
    }

    public String getHomeTeamLogo() {
        return _homeTeamLogo;
    }

    public String getAwayTeamLogo() {
        return _awayTeamLogo;
    }

    public int getHomeTeamStatsId() {
        return _homeTeamStatsId;
    }

    public int getAwayTeamStatsId() {
        return _awayTeamStatsId;
    }

    public boolean isAwayTeamPredicted(){
        return _isAwayPredicted;
    }

    public boolean isHomePredicted(){
        return _isHomePredicted;
    }
}
