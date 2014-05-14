package com.fantasysport.webaccess.responses;

import com.fantasysport.models.NFGame;

import java.util.List;

/**
 * Created by bylynka on 5/13/14.
 */
public class GeTNFGamesResponse {

    public List<NFGame> _games;

    public List<NFGame> getGames(){
        return _games;
    }
}
