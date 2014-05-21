package com.fantasysport.webaccess.responses;

import java.util.List;

/**
 * Created by bylynka on 5/13/14.
 */
public class GeTNFGamesResponse {

    public List<GetGamesResponse> _games;

    public List<GetGamesResponse> getGames(){
        return _games;
    }
}
