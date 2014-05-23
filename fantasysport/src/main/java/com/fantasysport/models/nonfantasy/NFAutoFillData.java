package com.fantasysport.models.nonfantasy;

import java.util.List;

/**
 * Created by bylynka on 5/23/14.
 */
public class NFAutoFillData {

    private List<NFTeam> _rosterTeams;

    private List<NFGame> _candidateGames;

    public NFAutoFillData(List<NFTeam> rosterTeams, List<NFGame> candidateGames){
        _rosterTeams = rosterTeams;
        _candidateGames = candidateGames;
    }

    public List<NFTeam> getRosterTeams(){
        return _rosterTeams;
    }

    public List<NFGame> getCandidateGames(){
        return _candidateGames;
    }

}
