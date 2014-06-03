package com.fantasysport.models.fwc;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 6/2/14.
 */
public class Player {

    @SerializedName("name")
    private String _name;

    @SerializedName("stats_id")
    private String _statsId;

    @SerializedName("team")
    private String _team;

    @SerializedName("headshot_url")
    private String _avaUrl;

    @SerializedName("pt")
    private double _pt;

    @SerializedName("disable_pt")
    private boolean _isPredicted;

    @SerializedName("logo_url")
    private String _logoUrl;

    public String getName() {
        return _name;
    }

    public String getStatsId() {
        return _statsId;
    }

    public String getTeam() {
        return _team;
    }

    public String getAvaUrl() {
        return _avaUrl;
    }

    public double getPT() {
        return _pt;
    }

    public boolean isPredicted() {
        return _isPredicted;
    }

    public String getLogoUrl() {
        return _logoUrl;
    }
}
