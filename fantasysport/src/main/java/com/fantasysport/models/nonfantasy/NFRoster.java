package com.fantasysport.models.nonfantasy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by bylynka on 5/16/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NFRoster {

    @JsonProperty("room_number")
    private int _roomNumber;

    @JsonProperty("state")
    private String _state;

    @JsonProperty("teams")
    private List<NFTeam> _teams;

    @JsonProperty("amount_paid")
    private Double _amountPaid;

    @JsonProperty("contest_rank")
    private Integer _contestRank;

    @JsonProperty("id")
    private Integer _id;

    public NFRoster(List<NFTeam> teams,
                    String state,
                    int roomNumber,
                    Double amountPaid,
                    Integer contestRank,
                    Integer id){
        _teams = teams;
        _state = state;
        _roomNumber = roomNumber;
        _amountPaid = amountPaid;
        _contestRank = contestRank;
        _id = id;
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

    public Integer getId() {
        return _id;
    }

    public enum State{
        Undefined,
        Submitted,
        Canceled,
        Finished
    }

}
