package com.fantasysport.webaccess.requests.fantasy;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by bylynka on 2/26/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateRosterRequestBody {

    @JsonProperty("market_id")
    private int _marketId;

    public CreateRosterRequestBody(int marketId){
        _marketId = marketId;
    }
}
