package com.fantasysport.models;

/**
 * Created by bylynka on 6/24/14.
 */
public enum  RosterType {
    Default("default"),
    Pick5("pick5"),
    HTT("100/30/30");

    private final String _name;

    private RosterType(String s){
        _name = s;
    }

    @Override
    public String toString() {
        return _name;
    }
}
