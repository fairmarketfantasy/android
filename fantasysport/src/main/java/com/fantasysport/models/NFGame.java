package com.fantasysport.models;

import com.fantasysport.adapters.nonfantasy.INFGame;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 5/13/14.
 */
public class NFGame {

    @SerializedName("game_stats_id")
    private int _statsId;

    @SerializedName("game_time")
    private String _gameTime;

    @SerializedName("home_team_name")
    private String _homeTeam;

    @SerializedName("away_team_name")
    private String _awayTeam;

    @SerializedName("home_team_pt")
    private int _homeTeamPt;

    @SerializedName("away_team_pt")
    private int _awayTeamPt;

    @SerializedName("home_team_stats_id")
    private int _homeTeamStatsId;

    @SerializedName("away_team_stats_id")
    private int _awayTeamStatsId;

    @SerializedName("home_team_logo_url")
    private String _homeTeamLogo;

    @SerializedName("away_team_logo_url")
    private String _awayTeamLogo;

    public int getStatsId() {
        return _statsId;
    }

    public void setStatsId(int statsId) {
        _statsId = statsId;
    }

    public String getGameTime() {
        return _gameTime;
    }

    public void setGameTime(String gameTime) {
        _gameTime = gameTime;
    }

    public String getHomeTeamName() {
        return _homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        _homeTeam = homeTeam;
    }

    public String getAwayTeamName() {
        return _awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        _awayTeam = awayTeam;
    }

    public int getHomeTeamPt() {
        return _homeTeamPt;
    }

    public void setHomeTeamPt(int homeTeamPt) {
        _homeTeamPt = homeTeamPt;
    }

    public int getAwayTeamPt() {
        return _awayTeamPt;
    }

    public void setAwayTeamPt(int awayTeamPt) {
        _awayTeamPt = awayTeamPt;
    }

    public String getHomeTeamLogo() {
        return _homeTeamLogo;
    }

    public void setHomeTeamLogo(String homeTeamLogo) {
        _homeTeamLogo = homeTeamLogo;
    }

    public String getAwayTeamLogo() {
        return _awayTeamLogo;
    }

    public void setAwayTeamLogo(String awayTeamLogo) {
        _awayTeamLogo = awayTeamLogo;
    }

    public int getHomeTeamStatsId() {
        return _homeTeamStatsId;
    }

    public int getAwayTeamStatsId() {
        return _awayTeamStatsId;
    }
}
