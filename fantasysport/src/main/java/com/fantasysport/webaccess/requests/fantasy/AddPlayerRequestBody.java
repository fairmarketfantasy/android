package com.fantasysport.webaccess.requests.fantasy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by bylynka on 2/27/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddPlayerRequestBody {

    @JsonProperty("price")
    private double _price;

    public AddPlayerRequestBody(double price){
        _price = price;
    }
}
