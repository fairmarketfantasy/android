package com.fantasysport.models;

import com.fantasysport.models.nonfantasy.NFPrediction;
import com.fantasysport.models.nonfantasy.NFTeam;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bylynka on 5/16/14.
 */
public class NFRoster {

    @SerializedName("room_number")
    private int _roomNumber;

    @SerializedName("state")
    private String _state;

    @SerializedName("teams")
    private List<NFTeam> _teams;

    @SerializedName("amount_paid")
    private Double _amountPaid;

    @SerializedName("contest_rank")
    private Integer _contestRank;

    public NFRoster(List<NFTeam> teams,
                    String state,
                    int roomNumber,
                    Double amountPaid,
                    Integer contestRank){
        _teams = teams;
        _state = state;
        _roomNumber = roomNumber;
        _amountPaid = amountPaid;
        _contestRank = contestRank;
    }

    public Double getAmountPaid(){
         return  _amountPaid;
    }

    public Integer getContestRank(){
        return _contestRank;
    }

    public State getState(){
        if(_state == null){
            return State.Undefined;
        }
        if(_state.equalsIgnoreCase("finished")){
            return State.Finished;
        }else if(_state.equalsIgnoreCase("submitted")){
            return State.Submitted;
        }else if(_state.equalsIgnoreCase("canceled")) {
            return State.Canceled;
        }
        return State.Undefined;
    }

    public int getRoomNumber(){
        return _roomNumber;
    }

    public List<NFTeam> getTeams(){
        return _teams;
    }

    public enum State{
        Undefined,
        Submitted,
        Canceled,
        Finished
    }

}
