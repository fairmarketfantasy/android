package com.fantasysport.parsers;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by bylynka on 2/26/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayersListWrapper {

    @JsonProperty("data")
    private List<Object> _playerList;

    public List<Object> getPlayerList(){
        return _playerList;
    }
}
