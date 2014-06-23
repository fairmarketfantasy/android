package com.fantasysport.webaccess.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by bylynka on 2/7/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class FacebookSignInRequestBody {

    @JsonProperty("access_token")
    private String _accessToken;

    public FacebookSignInRequestBody(String accessToken){
        _accessToken = accessToken;
    }

    public String getAccessToken(){
        return _accessToken;
    }
}
