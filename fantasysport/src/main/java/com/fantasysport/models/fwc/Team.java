package com.fantasysport.models.fwc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by bylynka on 6/2/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Team implements IFWCModel {

    @JsonProperty("name")
    private String _name;

    @JsonProperty("stats_id")
    private String _statsId;

    @JsonProperty("logo_url")
    private String _logoUrl;

    @JsonProperty("pt")
    private double _pt;

    @JsonProperty("game_stats_id")
    private String _gameStatsId;


    @JsonProperty("disable_pt")
    private boolean _isPredicted;

    public boolean isPredicted() {
        return _isPredicted;
    }

    @Override
    public void setIsPredicted(boolean isPredicted) {
        _isPredicted = isPredicted;
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
