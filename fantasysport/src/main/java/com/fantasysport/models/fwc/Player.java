package com.fantasysport.models.fwc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by bylynka on 6/2/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Player implements IFWCModel {

    @JsonProperty("name")
    private String _name;

    @JsonProperty("stats_id")
    private String _statsId;

    @JsonProperty("team")
    private String _team;

    @JsonProperty("headshot_url")
    private String _avaUrl;

    @JsonProperty("pt")
    private double _pt;

    @JsonProperty("disable_pt")
    private boolean _isPredicted;

    @JsonProperty("logo_url")
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

    @Override
    public void setIsPredicted(boolean isPredicted) {
        _isPredicted = isPredicted;
    }

    public String getLogoUrl() {
        return _logoUrl;
    }
}
