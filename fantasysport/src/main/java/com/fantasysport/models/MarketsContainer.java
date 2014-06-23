package com.fantasysport.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by bylynka on 3/31/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarketsContainer {

    @JsonProperty("updated_at")
    private long _updatedAt;

    @JsonProperty("markets")
    private List<Market> _markets;

    public long getUpdatedAt() {
        return _updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        _updatedAt = updatedAt;
    }

    public List<Market> getMarkets() {
        return _markets;
    }

    public void setMarkets(List<Market> markets) {
        _markets = markets;
    }
}
