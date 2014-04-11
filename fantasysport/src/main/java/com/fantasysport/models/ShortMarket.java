package com.fantasysport.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by bylynka on 4/11/14.
 */
public class ShortMarket implements Serializable{

    @SerializedName("id")
    private int _id;

    public int getId(){
        return _id;
    }
}
