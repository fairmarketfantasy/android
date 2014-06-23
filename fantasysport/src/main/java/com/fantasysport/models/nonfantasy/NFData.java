package com.fantasysport.models.nonfantasy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by bylynka on 5/16/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NFData {

    @JsonProperty("updated_at")
    private long _updatedAt;

    @JsonProperty("game_roster")
    private NFRoster _roster;

    @JsonProperty("games")
    private List<NFGame> _candidateGames;

    public NFRoster getRoster(){
        return _roster;
    }

    public List<NFGame> getCandidateGames(){
        return _candidateGames;
    }

    public NFData(NFRoster roster, List<NFGame> games){
        _candidateGames = games;
        _roster = roster;
    }

    public void setUpdatedAt(long updatedAt){
        _updatedAt = updatedAt;
    }

    public long getUpdatedAt(){
        return _updatedAt;
    }
}
