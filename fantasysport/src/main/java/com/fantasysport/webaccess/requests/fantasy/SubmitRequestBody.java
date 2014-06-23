package com.fantasysport.webaccess.requests.fantasy;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by bylynka on 3/4/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmitRequestBody {

    @JsonProperty("contest_type")
    private String _contestType;

    public SubmitRequestBody(String contestType){
        _contestType = contestType;
    }
}
