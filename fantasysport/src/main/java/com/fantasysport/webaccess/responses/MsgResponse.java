package com.fantasysport.webaccess.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by bylynka on 5/26/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MsgResponse {

    @JsonProperty("msg")
    private String _msg;

    public String getMessage(){
        return _msg;
    }
}
