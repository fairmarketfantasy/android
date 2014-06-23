package com.fantasysport.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by bylynka on 5/7/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sport {
    public static final String MLB = "MLB";
    public static final String NBA = "NBA";

    @JsonProperty("title")
    private String _title;

    @JsonProperty("name")
    private String _name_key;

    @JsonProperty("coming_soon")
    private boolean _comingSoon;

    @JsonProperty("is_active")
    private boolean _isActive;

    public String getNameKey(){
        return _name_key;
    }

    public boolean comingSoon(){
        return _comingSoon;
    }

    public boolean isActive(){
        return _isActive;
    }

    public String getTitle(){
        return _title;
    }
}
