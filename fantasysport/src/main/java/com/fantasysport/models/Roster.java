package com.fantasysport.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bylynka on 2/7/14.
 */
public class Roster  implements Serializable {

    public final static String SUBMITTED = "submitted";
    public static final String IN_PROGRESS = "in_progress";

    @SerializedName("id")
    private int _id;

    @SerializedName("remaining_salary")
    private double _remainingSalary;

    @SerializedName("players")
    private List<Player> _players;

    @SerializedName("contest_rank")
    private int _contestRank;

    @SerializedName("amount_paid")
    private double _amountPaid;

    @SerializedName("state")
    private String _state;

    @SerializedName("market")
    private ShortMarket _market;

    public int getContestRank(){
        return _contestRank;
    }

    public double getAmountPaid(){
        return _amountPaid;
    }

    public List<Player> getPlayers(){
        return _players;
    }

    public void setPlayers(List<Player> players){
        _players = players;
    }

    public double getRemainingSalary(){
        return _remainingSalary;
    }

    public void setRemainingSalary(double remainingSalary){
        _remainingSalary = remainingSalary;
    }

    public int getId(){
        return _id;
    }

    public String getState(){
        return _state;
    }

    public int getMarketId(){
        return _market.getId();
    }
}
