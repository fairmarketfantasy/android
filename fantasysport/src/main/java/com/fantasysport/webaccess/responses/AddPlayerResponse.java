package com.fantasysport.webaccess.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 2/27/14.
 */
public class AddPlayerResponse {

    @SerializedName("price")
    private double _price;

    public double getPrice(){
        return _price;
    }
}
