package com.fantasysport.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 2/13/14.
 */
public class AccessTokenData {

    @SerializedName("access_token")
    private String _accessToken;

    @SerializedName("token_type")
    private String _tokenType;

    @SerializedName("refresh_token")
    private String _refreshToken;

    @SerializedName("expires_in")
    private int _expiresIn;

    public String getAccessToken(){
        return _accessToken;
    }

    public String getTokenType(){
        return _tokenType;
    }

    public String getRefreshToken(){
        return _refreshToken;
    }

    public int getExpiresIn(){
        return _expiresIn;
    }
}
