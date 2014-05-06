package com.fantasysport.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by bylynka on 4/16/14.
 */
public class Position implements Serializable {

    @SerializedName("acronym")
    private String _acronym;

    @SerializedName("name")
    private String _name;

    public String getAcronym(){
        return _acronym;
    }

    public String getName(){
        return _name;
    }

    @Override
    public int hashCode() {
        if(_acronym == null){
            return 0;
        }
        return _acronym.hashCode();
    }
    @Override
    public boolean equals(Object o) {
       return hashCode() == o.hashCode();
    }
}
