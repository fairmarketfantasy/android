package com.fantasysport.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bylynka on 3/31/14.
 */
public class MarketsContainer {

    @SerializedName("updated_at")
    private long _updatedAt;

    @SerializedName("markets")
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
