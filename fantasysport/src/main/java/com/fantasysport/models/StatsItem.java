package com.fantasysport.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by bylynka on 3/5/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatsItem implements Serializable {

    public final static String LESS_MODE = "less";
    public final static String MORE_MODE = "more";
    public final static String DEFAULT_MODE = "none";

    @JsonProperty("name")
    private String _name;

    @JsonProperty("value")
    private double _value;

    @JsonProperty("bid_less")
    private boolean _bidLess;

    @JsonProperty("bid_more")
    private boolean _bidMore;

    @JsonProperty("diff")
    private String _mode = DEFAULT_MODE;

    public StatsItem(){
    }

    public boolean getBidMore(){
        return _bidMore;
    }

    public boolean getBidLess(){
        return _bidLess;
    }

    public StatsItem(String name, int value){
        _name = name;
        _value = value;
    }

    public String getName(){
        return _name;
    }

    public void setName(String name){
        _name = name;
    }

    public double getValue(){
        return _value;
    }

    public void setValue(double value){
        _value = value;
    }

    public void setMode(String mode){
        _mode = mode;
    }

    public String getMode(){
        return _mode;
    }

    public void setBidLess(boolean bidLess) {
        _bidLess = bidLess;
    }

    public void setBidMore(boolean bidMore) {
        _bidMore = bidMore;
    }
}
