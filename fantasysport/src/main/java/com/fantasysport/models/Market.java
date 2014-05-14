package com.fantasysport.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by bylynka on 2/20/14.
 */
public class Market  implements Serializable {

    @SerializedName("id")
    private int _id;

    @SerializedName("name")
    private String _name;

//    @SerializedName("shadow_bets")
//    private double _shadowBets;

//    @SerializedName("shadow_bet_rate")
//    private double _shadowBetRate;

    @SerializedName("sport_id")
    private int _sportId;

    @SerializedName("state")
    private String _state;

//    @SerializedName("market_duration")
//    private String _marketDuration;

    @SerializedName("game_type")
    private String _gameType;

    @SerializedName("games")
    private List<FGame> _games;

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

//    public void setShadowBets(double bets){
//        _shadowBets = bets;
//    }
//
//    public double getShadowBets(){
//        return _shadowBets;
//    }

//    public void  setShadowBetRate(double rate){
//        _shadowBetRate = rate;
//    }
//
//    public double getShadowBetRate(){
//        return _shadowBetRate;
//    }

    public Date getStartedAt(){
        return  _games.get(0).getGameTime();
    }

    public void setSportId(int sportId){
        _sportId = sportId;
    }

    public int getSportId(){
        return _sportId;
    }

    public void setState(String state){
        _state = state;
    }

    public String getState(){
        return _state;
    }

//    public void setMarketDuration(String duration){
//        _marketDuration = duration;
//    }
//
//    public String getMarketDuration(){
//        return _marketDuration;
//    }

    public void setGameType(String type){
        _gameType = type;
    }

    public String  getGameType(){
        return _gameType;
    }

    public void setGames(List<FGame> games){
        _games = games;
    }

    public List<FGame> getGames(){
        return _games;
    }

}
