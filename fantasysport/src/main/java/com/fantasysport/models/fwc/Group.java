package com.fantasysport.models.fwc;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bylynka on 6/2/14.
 */
public class Group {

    @SerializedName("name")
    private String _name;

    @SerializedName("teams")
    private List<Team> _teams;

    public String getName(){
        return _name;
    }

    public List<Team> getTeams(){
        return _teams;
    }

}
