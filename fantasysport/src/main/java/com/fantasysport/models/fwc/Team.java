package com.fantasysport.models.fwc;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 6/2/14.
 */
public class Team {

    @SerializedName("name")
    private String _name;

    @SerializedName("stats_id")
    private String _statsId;

    @SerializedName("logo_url")
    private String _logoUrl;

    @SerializedName("pt")
    private double _pt;

    @SerializedName("game_stats_id")
    private String _gameStatsId;


    @SerializedName("disable_pt")
    private boolean _isPredicted;

    public boolean isPredicted() {
        return _isPredicted;
    }

    public String getGameStatsId() {
        return _gameStatsId;
    }

    public double getPT() {
        return _pt;
    }

    public String getLogoUrl() {
        return _logoUrl;
    }

    public String getStatsId() {
        return _statsId;
    }

    public String getName() {
        return _name;
    }

}
