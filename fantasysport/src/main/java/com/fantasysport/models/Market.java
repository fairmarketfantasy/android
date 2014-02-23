package com.fantasysport.models;

import android.support.v7.appcompat.R;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by bylynka on 2/20/14.
 */
public class Market {

    @SerializedName("id")
    private int _id;

    @SerializedName("name")
    private String _name;

    @SerializedName("shadow_bets")
    private double _shadowBets;

    @SerializedName("shadow_bet_rate")
    private double _shadowBetRate;

    @SerializedName("started_at")
    private Date _startedAt;

    @SerializedName("opened_at")
    private Date _openedAt;

    @SerializedName("closed_at")
    private Date _closedAt;

    @SerializedName("sport_id")
    private int _sportId;

    @SerializedName("total_bets")
    private double _totalBets;

    @SerializedName("state")
    private String _state;

    @SerializedName("market_duration")
    private String _marketDuration;

    @SerializedName("game_type")
    private String _gameType;

    @SerializedName("games")
    private List<Game> _games;

    public void setId(int id){
        _id = id;
    }

    public int getId(){
        return _id;
    }

    public void setName(String name){
        _name = name;
    }

    public String getName(){
        return _name;
    }

    public void setShadowBets(double bets){
        _shadowBets = bets;
    }

    public double getShadowBets(){
        return _shadowBets;
    }

    public void  setShadowBetRate(double rate){
        _shadowBetRate = rate;
    }

    public double getShadowBetRate(){
        return _shadowBetRate;
    }

    public void setStartedAt(Date date){
        _startedAt = date;
    }

    public Date getStartedAt(){
        return  _startedAt;
    }

    public void setOpenedAt(Date date){
        _openedAt = date;
    }

    public Date getOpenedAt(){
        return _openedAt;
    }

    public void setClosedAt(Date date){
        _closedAt = date;
    }

    public Date getClosedAt(){
        return _closedAt;
    }

    public void setSportId(int sportId){
        _sportId = sportId;
    }

    public int getSportId(){
        return _sportId;
    }

    public void  setTotalBets(double totalBets){
        _totalBets = totalBets;
    }

    public double getTotalBets(){
        return _totalBets;
    }

    public void setState(String state){
        _state = state;
    }

    public String getState(){
        return _state;
    }

    public void setMarketDuration(String duration){
        _marketDuration = duration;
    }

    public String getMarketDuration(){
        return _marketDuration;
    }

    public void setGameType(String type){
        _gameType = type;
    }

    public String  getGameType(){
        return _gameType;
    }

    public void setGames(List<Game> games){
        _games = games;
    }

    public List<Game> getGames(){
        return _games;
    }

}
