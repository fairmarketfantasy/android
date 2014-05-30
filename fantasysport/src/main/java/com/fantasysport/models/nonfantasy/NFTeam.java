package com.fantasysport.models.nonfantasy;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by bylynka on 5/21/14.
 */
public class NFTeam implements Serializable, INFTeam {

    private int _gameStatsId;

    private String _name;

    private double _pt;

    private int _statsId;

    private String _logoUrl;

    private boolean _isSelected = false;

    private boolean _isPredicted = false;

    private String _gameName;

    private Date _date;

    public NFTeam(){
    }

    public NFTeam(String name,
                  double pt,
                  int statsId,
                  String logoUrl,
                  int gameStatsId,
                  String gameName,
                  Date date){
        _name = name;
        _pt = pt;
        _statsId = statsId;
        _logoUrl = logoUrl;
        _gameStatsId = gameStatsId;
        _gameName = gameName;
        _date = date;
    }

    public Date getDate(){
        return _date;
    }

    public String getGameName(){
        return _gameName;
    }

    public int getGameStatsId(){
        return _gameStatsId;
    }

    public String getName(){
        return _name;
    }

    public double getPT(){
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

    public boolean isPredicted(){
        return _isPredicted;
    }

    public void setIsPredicted(boolean isPredicted) {
        _isPredicted = isPredicted;
    }
}
