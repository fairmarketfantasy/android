package com.fantasysport.webaccess.responses;

import com.fantasysport.models.fantasy.Player;

import java.util.List;

/**
 * Created by bylynka on 2/26/14.
 */
public class PlayersRequestResponse {

    private List<Player> _players;

    public PlayersRequestResponse(List<Player> players){
        _players = players;
    }

    public List<Player> getPlayers(){
        return _players;
    }
}
