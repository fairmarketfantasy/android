package com.fantasysport.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by bylynka on 3/5/14.
 */
public class StatsItem implements Serializable {

    public final static String LESS_MODE = "less";
    public final static String MORE_MODE = "more";
    public final static String DEFAULT_MODE = "none";

    @SerializedName("name")
    private String _name;

    @SerializedName("value")
    private double _value;

    @SerializedName("diff")
    private String _mode = DEFAULT_MODE;

    public StatsItem(){
    }

    public StatsItem(String name, int value){
        _name = name;
        _value = value;
    }

    public String getName(){
        return _name;
    }

    public double getValue(){
        return _value;
    }

    public void setMode(String mode){
        _mode = mode;
    }

    public String getMode(){
        return _mode;
    }

}
