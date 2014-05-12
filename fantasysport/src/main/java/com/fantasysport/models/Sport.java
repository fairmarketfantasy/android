package com.fantasysport.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 5/7/14.
 */
public class Sport {
    public static final String MLB = "MLB";
    public static final String NBA = "NBA";

    @SerializedName("name")
    private String _name;

    @SerializedName("coming_soon")
    private boolean _comingSoon;

    @SerializedName("is_active")
    private boolean _isActive;

    public String getName(){
        return _name;
    }

    public boolean comingSoon(){
        return _comingSoon;
    }

    public boolean isActive(){
        return _isActive;
    }
}
