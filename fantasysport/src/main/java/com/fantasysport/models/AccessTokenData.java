package com.fantasysport.models;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.Date;

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

    @SerializedName("create_time")
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
