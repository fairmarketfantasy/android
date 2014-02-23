package com.fantasysport.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by bylynka on 2/20/14.
 */
public class Game {

    @SerializedName("id")
    private String _id;

    @SerializedName("stats_id")
    private String _statsId;

    @SerializedName("status")
    private String _status;

    @SerializedName("game_day")
    private Date _gameDay;

    @SerializedName("game_time")
    private Date _gameTime;

    @SerializedName("home_team")
    private String _homeTeam;

    @SerializedName("away_team")
    private String _awayTeam;

    @SerializedName("season_type")
    private String _seasonType;

    @SerializedName("season_week")
    private int _seasonWeek;

    @SerializedName("season_year")
    private int _seasonYear;

    @SerializedName("network")
    private String _network;

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getStatsId() {
        return _statsId;
    }

    public void setStatsId(String statsId) {
        _statsId = statsId;
    }

    public String getStatus() {
        return _status;
    }

    public void setStatus(String status) {
        _status = status;
    }

    public Date getGameDay() {
        return _gameDay;
    }

    public void setGameDay(Date gameDay) {
        _gameDay = gameDay;
    }

    public Date getGameTime() {
        return _gameTime;
    }

    public void setGameTime(Date gameTime) {
        _gameTime = gameTime;
    }

    public String getHomeTeam() {
        return _homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        _homeTeam = homeTeam;
    }

    public String get_awayTeam() {
        return _awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        _awayTeam = awayTeam;
    }

    public String getSeasonType() {
        return _seasonType;
    }

    public void setSeasonType(String seasonType) {
        _seasonType = seasonType;
    }

    public int getSeasonWeek() {
        return _seasonWeek;
    }

    public void setSeasonWeek(int seasonWeek) {
        _seasonWeek = seasonWeek;
    }

    public int getSeasonYear() {
        return _seasonYear;
    }

    public void setSeasonYear(int seasonYear) {
        _seasonYear = seasonYear;
    }

    public String getNetwork() {
        return _network;
    }

    public void setNetwork(String network) {
        _network = network;
    }
}
