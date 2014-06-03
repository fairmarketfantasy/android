package com.fantasysport.models.nonfantasy;

import com.fantasysport.models.NFRoster;
import com.fantasysport.models.nonfantasy.NFGame;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bylynka on 5/16/14.
 */
public class NFData {

    @SerializedName("updated_at")
    private long _updatedAt;

    @SerializedName("game_roster")
    private NFRoster _roster;

    @SerializedName("games")
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
