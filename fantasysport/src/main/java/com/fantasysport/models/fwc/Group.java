package com.fantasysport.models.fwc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by bylynka on 6/2/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Group {

    @JsonProperty("name")
    private String _name;

    @JsonProperty("teams")
    private List<Team> _teams;

    public String getName(){
        return _name;
    }

    public List<Team> getTeams(){
        return _teams;
    }

}
