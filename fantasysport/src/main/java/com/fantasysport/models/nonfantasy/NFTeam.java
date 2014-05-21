package com.fantasysport.models.nonfantasy;

import java.io.Serializable;

/**
 * Created by bylynka on 5/21/14.
 */
public class NFTeam implements Serializable, INFTeam {

    private int _gameStatsId;

    private String _name;

    private int _pt;

    private int _statsId;

    private String _logoUrl;

    private boolean _isSelected = false;

    public NFTeam(){
    }

    public NFTeam(String name, int pt, int statsId, String logoUrl, int gameStatsId){
        _name = name;
        _pt = pt;
        _statsId = statsId;
        _logoUrl = logoUrl;
        _gameStatsId = gameStatsId;
    }

    public int getGameStatsId(){
        return _gameStatsId;
    }

    public String getName(){
        return _name;
    }

    public int getPT(){
        return _pt;
    }

    public void setIsSelected(boolean isSelected){
        _isSelected = isSelected;
    }

    public boolean isSelected(){
        return _isSelected;
    }

    public int getStatsId(){
        return _statsId;
    }

    public String getLogoUrl(){
        return _logoUrl;
    }


}
