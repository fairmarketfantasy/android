package com.fantasysport.models;

import com.fantasysport.models.fantasy.Player;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bylynka on 2/7/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Roster  implements Serializable {

    public final static String SUBMITTED = "submitted";
    public static final String IN_PROGRESS = "in_progress";

    @JsonProperty("id")
    private int _id;

    @JsonProperty("remaining_salary")
    private double _remainingSalary;

    @JsonProperty("players")
    private List<Player> _players;

    @JsonProperty("contest_rank")
    private int _contestRank;

    @JsonProperty("amount_paid")
    private double _amountPaid;

    @JsonProperty("state")
    private String _state;

    @JsonProperty("market")
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
