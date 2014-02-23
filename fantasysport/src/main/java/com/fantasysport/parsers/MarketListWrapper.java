package com.fantasysport.parsers;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bylynka on 2/19/14.
 */
public class MarketListWrapper {

    @SerializedName("data")
    private List<Object> _marketList;

    public List<Object> getMarketList(){
        return _marketList;
    }
}
