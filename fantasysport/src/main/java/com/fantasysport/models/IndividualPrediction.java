package com.fantasysport.models;

import java.util.Date;
import java.util.List;

/**
 * Created by bylynka on 3/19/14.
 */
public class IndividualPrediction {

    public final static String CANCELED = "canceled";
    public final static String FINISHED = "finished";

    private String _marketName;
    private String _playerName;
    private Date _gameData;
    private double _award;
    private List<StatsItem> _eventPredictions;
    private double _pt;
    private String _state;
    private double _gameResult;
    private int _id;

    public boolean isEmpty(){
        return _eventPredictions == null;
    }

    public String getMarketName() {
        return _marketName;
    }

    public void setMarketName(String marketName) {
        _marketName = marketName;
    }

    public String getPlayerName() {
        return _playerName;
    }

    public void setPlayerName(String playerName) {
        _playerName = playerName;
    }

    public Date getGameData() {
        return _gameData;
    }

    public void setGameData(Date gameData) {
        _gameData = gameData;
    }

    public double getAward() {
        return _award;
    }

    public void setAward(double award) {
        _award = award;
    }

    public List<StatsItem> getEventPredictions() {
        return _eventPredictions;
    }

    public void setEventPredictions(List<StatsItem> eventPredictions) {
        _eventPredictions = eventPredictions;
    }

    public double getPT() {
        return _pt;
    }

    public void setPT(double pt) {
        _pt = pt;
    }

    public void setState(String state) {
        _state = state;
    }

    public String getState() {
        return _state;
    }

    public void setGameResult(double gameResult) {
        this._gameResult = gameResult;
    }

    public double getGameResult() {
        return _gameResult;
    }

    public void setId(int id) {
        _id = id;
    }

    public int getId() {
        return _id;
    }
}
