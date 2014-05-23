package com.fantasysport.adapters.fantasy;

import com.fantasysport.models.IPlayer;

/**
 * Created by bylynka on 2/25/14.
 */
public class PlayerItem implements IPlayer {

    private String _position;

    public PlayerItem(String position){
        _position = position;
    }

    public String getPosition(){
        return _position;
    }

}
