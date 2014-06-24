package com.fantasysport.models.nonfantasy;

import com.fantasysport.models.RosterType;
import com.fantasysport.parsers.jackson.DateDeserializer;
import com.fantasysport.parsers.jackson.DateSerializer;
import com.fantasysport.parsers.jackson.RosterTypeDeserializer;
import com.fantasysport.parsers.jackson.RosterTypeSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by bylynka on 5/27/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NFPrediction implements Serializable {

    public final String SUBMITTED = "submitted";
    public final String FINISHED = "finished";
    public final String CANCELED = "canceled";

    @JsonProperty("id")
    private int _id = -1;

    @JsonProperty("state")
    private String _state;

    @JsonProperty("contest_rank")
    private Integer _rank;

    @JsonProperty("score")
    private Double _points;

    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonProperty("started_at")
    private Date _date;

    @JsonSerialize(using = DateSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    @JsonProperty("cancelled_at")
    private Date _newDate;

    @JsonProperty("contest_rank_payout")
    private Double _award;

    @JsonDeserialize(using = RosterTypeDeserializer.class)
    @JsonSerialize(using = RosterTypeSerializer.class)
    @JsonProperty("roster_type")
    private RosterType _rosterType;

    @JsonProperty("contest_type")
    private ContestType _contestType;

    public String getName(){
        return _contestType != null? _contestType.getName(): "";
    }

    public Date getNewDate(){
        return _newDate;
    }

    public Double getPoints(){
        return _points;
    }

    public Date getDate() {
        return _date;
    }

    public Integer getRank(){
        return _rank;
    }

    public int getId(){
        return _id;
    }

    public RosterType getRosterType(){
       return _rosterType;
    }

    public State getState(){
        if(_state.equalsIgnoreCase(SUBMITTED)){
            return State.Submitted;
        }else if(_state.equalsIgnoreCase(CANCELED)){
            return State.Canceled;
        }else {
            return State.Finished;
        }
    }

    public boolean isEmpty(){
      return _id < 0;
    }

    public Double getAward() {
        return _award == null? 0: _award/100;
    }

    public static enum State{
        Submitted,
        Finished,
        Canceled
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContestType{

        @JsonProperty("name")
        private String _name;

        public String getName(){
            return _name;
        }

    }

}
