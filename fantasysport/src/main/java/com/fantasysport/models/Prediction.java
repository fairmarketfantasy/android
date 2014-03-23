package com.fantasysport.models;

import java.util.Date;

/**
 * Created by bylynka on 3/19/14.
 */
public class Prediction {

    private Market _market;
    private int _rank;
    private double _score;
    private String _state;
    private String _contestType;
    private int _maxEntries;


    public boolean isEmpty(){
        return _market == null;
    }

    public int getMaxEntries(){
        return _maxEntries;
    }

    public void setMaxEntries(int maxEntries){
        _maxEntries = maxEntries;
    }

    public String getContestType(){
        return _contestType;
    }

    public void setContestType(String contestType){
        _contestType = contestType;
    }

    public Market getMarket() {
        return _market;
    }

    public void setMarket(Market market) {
        _market = market;
    }

    public int getRank() {
        return _rank;
    }

    public void setRank(int rank) {
        _rank = rank;
    }

    public double getScore() {
        return _score;
    }

    public void setScore(double score) {
        _score = score;
    }

    public String getState() {
        return _state;
    }

    public void setState(String state) {
        _state = state;
    }

    public Date getStartedAt() {
        return _market.getStartedAt();
    }

}
