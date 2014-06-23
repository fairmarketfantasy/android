package com.fantasysport.webaccess.requests.fantasy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by bylynka on 3/3/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TradePlayerRequestBody {

    @JsonProperty("price")
    private double _price;

    public TradePlayerRequestBody(double price){
        _price = price;
    }
}
