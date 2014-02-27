package com.fantasysport.parsers;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bylynka on 2/26/14.
 */
public class PlayersListWrapper {

    @SerializedName("data")
    private List<Object> _playerList;

    public List<Object> getPlayerList(){
        return _playerList;
    }
}
