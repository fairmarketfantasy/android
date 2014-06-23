package com.fantasysport.models.fantasy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by bylynka on 4/16/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Position implements Serializable {

    @JsonProperty("acronym")
    private String _acronym;

    @JsonProperty("name")
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
