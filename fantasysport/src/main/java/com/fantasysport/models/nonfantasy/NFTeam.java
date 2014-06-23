package com.fantasysport.models.nonfantasy;

import com.fantasysport.parsers.jackson.DateDeserializer;
import com.fantasysport.parsers.jackson.DateSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by bylynka on 5/21/14.
 */
public class NFTeam implements Serializable, INFTeam {

    @JsonProperty("game_stats_id")
    private String _gameStatsId;

    @JsonProperty("name")
    private String _name;

    @JsonProperty("pt")
    private double _pt;

    @JsonProperty("stats_id")
    private String _statsId;

    @JsonProperty("logo_url")
    private String _logoUrl;

    @JsonProperty("is_added")
    private boolean _isSelected = false;

    @JsonProperty("disable_pt")
    private boolean _isPredicted = false;

    @JsonProperty("game_name")
    private String _gameName;

    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonProperty("game_time")
    private Date _date;

    public NFTeam(){
    }

    public NFTeam(String name,
                  double pt,
                  String statsId,
                  String logoUrl,
                  String gameStatsId,
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

    public String getGameStatsId(){
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

    public String getStatsId(){
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
