package com.fantasysport.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 5/16/14.
 */
public class NFRoster {

    @SerializedName("room_number")
    private int _roomNumber;

    @SerializedName("state")
    private String _state;

    public String getState(){
        return _state;
    }

    public int getRoomNumber(){
        return _roomNumber;
    }
}
