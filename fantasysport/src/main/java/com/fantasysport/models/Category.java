package com.fantasysport.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by bylynka on 5/12/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Category {

    @JsonProperty("title")
    private String _title;

    @JsonProperty("name")
    private String _name_key;

    @JsonProperty("note")
    private String _note;

    @JsonProperty("sports")
    private List<Sport> _sports;

    public String getNameKey(){
        return _name_key;
    }

    public List<Sport> getSports(){
        return _sports;
    }

    public boolean isActive(){
        return _note == null || _note.length() == 0;
    }

    public String getTitle(){
        return _title;
    }

}
