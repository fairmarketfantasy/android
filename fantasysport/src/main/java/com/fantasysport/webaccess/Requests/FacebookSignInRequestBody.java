package com.fantasysport.webaccess.Requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 2/7/14.
 */
class FacebookSignInRequestBody {

    @SerializedName("access_token")
    private String _accessToken;

    public FacebookSignInRequestBody(String accessToken){
        _accessToken = accessToken;
    }

    public String getAccessToken(){
        return _accessToken;
    }
}
