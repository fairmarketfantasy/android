package com.fantasysport.models.nonfantasy;

import com.fantasysport.utility.Converter;
import com.fantasysport.utility.DateUtils;
import com.fantasysport.utility.DeviceInfo;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by bylynka on 5/21/14.
 */
public class NFTeam implements Serializable, INFTeam {

    @SerializedName("game_stats_id")
    private String _gameStatsId;

    @SerializedName("name")
    private String _name;

    @SerializedName("pt")
    private double _pt;

    @SerializedName("stats_id")
    private String _statsId;

    @SerializedName("logo_url")
    private String _logoUrl;

    @SerializedName("is_added")
    private boolean _isSelected = false;

    @SerializedName("disable_pt")
    private boolean _isPredicted = false;

    @SerializedName("game_name")
    private String _gameName;

    @SerializedName("game_time")
    private String _date;

    public NFTeam(){
    }

    public NFTeam(String name,
                  double pt,
                  String statsId,
                  String logoUrl,
                  String gameStatsId,
                  String gameName,
                  String date){
        _name = name;
        _pt = pt;
        _statsId = statsId;
        _logoUrl = logoUrl;
        _gameStatsId = gameStatsId;
        _gameName = gameName;
        _date = date;
    }

    public Date getDate(){
        Date date = Converter.toDate(_date);
        int gmtInMinutes = DeviceInfo.getGMTInMinutes();
        return DateUtils.addMinutes(date, gmtInMinutes);
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
