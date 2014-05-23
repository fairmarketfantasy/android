package com.fantasysport.webaccess.requests.fantasy;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 3/3/14.
 */
public class TradePlayerRequestBody {

    @SerializedName("price")
    private double _price;

    public TradePlayerRequestBody(double price){
        _price = price;
    }
}
