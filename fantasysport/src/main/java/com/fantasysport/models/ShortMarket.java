package com.fantasysport.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by bylynka on 4/11/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShortMarket implements Serializable{

    @JsonProperty("id")
    private int _id;

    public int getId(){
        return _id;
    }
}
