package com.fantasysport.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by bylynka on 2/13/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessTokenData {

    @JsonProperty("access_token")
    private String _accessToken;

    @JsonProperty("token_type")
    private String _tokenType;

    @JsonProperty("refresh_token")
    private String _refreshToken;

    @JsonProperty("expires_in")
    private int _expiresIn;

    @JsonProperty("create_time")
    private long _createTime;

    public AccessTokenData(){
    }

    public void setCreateTime(long time){
        _createTime = time;
    }

    public long getCreateTime(){
        return _createTime;
    }

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
