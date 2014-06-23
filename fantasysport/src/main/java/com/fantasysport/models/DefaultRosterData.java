package com.fantasysport.models;

import com.fantasysport.models.fantasy.Position;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bylynka on 2/26/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DefaultRosterData implements Serializable {

    @JsonProperty("remaining_salary")
    private double _remainingSalary;

    @JsonProperty("positions")
    private List<Position> _positions;

    public List<Position> getPositions(){
        return _positions;
    }

    public double getRemainingSalary(){
        return _remainingSalary;
    }
}