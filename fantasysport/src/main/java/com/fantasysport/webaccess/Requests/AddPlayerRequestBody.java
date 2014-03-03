package com.fantasysport.webaccess.Requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 2/27/14.
 */
public class AddPlayerRequestBody {

    @SerializedName("price")
    private double _price;

    public AddPlayerRequestBody(double price){
        _price = price;
    }
}
