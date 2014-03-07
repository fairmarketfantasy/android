package com.fantasysport.webaccess.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 3/3/14.
 */
public class TradePlayerResponse {

    @SerializedName("price")
    private double _price;

    public double getPrice(){
        return _price;
    }
}
