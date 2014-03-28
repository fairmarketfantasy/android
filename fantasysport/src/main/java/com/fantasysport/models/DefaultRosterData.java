package com.fantasysport.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bylynka on 2/26/14.
 */
public class DefaultRosterData implements Serializable {

    @SerializedName("remaining_salary")
    private double _remainingSalary;

    @SerializedName("positions")
    private List<String> _positions;

    public List<String> getPositions(){
        return _positions;
    }

    public double getRemainingSalary(){
        return _remainingSalary;
    }
}