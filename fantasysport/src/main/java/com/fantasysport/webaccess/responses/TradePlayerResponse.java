package com.fantasysport.webaccess.responses;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by bylynka on 3/3/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TradePlayerResponse {

    @JsonProperty("price")
    private double _price;

    public double getPrice(){
        return _price;
    }
}
