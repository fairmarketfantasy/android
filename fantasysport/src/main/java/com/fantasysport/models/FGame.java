package com.fantasysport.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by bylynka on 2/20/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FGame implements Serializable, Comparable<FGame> {

//    @SerializedName("id")
//    private String _id;
//
//    @SerializedName("stats_id")
//    private String _statsId;

//    @SerializedName("status")
//    private String _status;

    @JsonProperty("game_time")
    private Date _gameTime;

    @JsonProperty("home_team")
    private String _homeTeam;

    @JsonProperty("away_team")
    private String _awayTeam;

//    public String getId() {
//        return _id;
//    }
//
//    public void setId(String _id) {
//        this._id = _id;
//    }
//
//    public String getStatsId() {
//        return _statsId;
//    }
//
//    public void setStatsId(String statsId) {
//        _statsId = statsId;
//    }

//    public String getStatus() {
//        return _status;
//    }
//
//    public void setStatus(String status) {
//        _status = status;
//    }

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

    public String getAwayTeam() {
        return _awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        _awayTeam = awayTeam;
    }

    @Override
    public int compareTo(FGame another) {
        return  _gameTime.compareTo(another._gameTime);
    }
}
