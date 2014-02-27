package com.fantasysport.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bylynka on 2/7/14.
 */
public class Roster {

    @SerializedName("id")
    private int _id;

    @SerializedName("remaining_salary")
    private double _remainingSalary;

    @SerializedName("players")
    private List<Player> _players;

    public List<Player> getPlayers(){
        return _players;
    }

    public double getRemainingSalary(){
        return _remainingSalary;
    }

    public int getId(){
        return _id;
    }

}
