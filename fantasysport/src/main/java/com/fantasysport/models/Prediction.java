package com.fantasysport.models;

import java.util.Date;

/**
 * Created by bylynka on 3/19/14.
 */
public class Prediction {

    public static final String FINISHED = "finished";
    public static final String SUBMITTED = "submitted";
    public static final String CANCELED = "cancelled";

    private Market _market;
    private int _rank;
    private double _score;
    private String _state;
    private String _contestType;
    private int _maxEntries;
    private int _id;
    private double _award;


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

    public void setId(int id){
        _id = id;
    }

    public int getId() {
        return _id;
    }

    public double getAward() {
        return _award;
    }

    public void setAward(double award){
        _award = award;
    }
}
