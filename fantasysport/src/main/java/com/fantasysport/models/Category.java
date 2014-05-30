package com.fantasysport.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bylynka on 5/12/14.
 */
public class Category {

    @SerializedName("title")
    private String _title;

    @SerializedName("name")
    private String _name_key;

    @SerializedName("note")
    private String _note;

    @SerializedName("sports")
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
