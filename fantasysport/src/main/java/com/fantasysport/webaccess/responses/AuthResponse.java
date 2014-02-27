package com.fantasysport.webaccess.responses;

import com.fantasysport.models.AccessTokenData;
import com.fantasysport.models.UserData;

/**
 * Created by bylynka on 2/25/14.
 */
public class AuthResponse {

    private UserData _userData;
    private AccessTokenData _accessTokenData;

    public AuthResponse(UserData userData, AccessTokenData accessTokenData){
        _accessTokenData = accessTokenData;
        _userData = userData;
    }

    public void setUserData(UserData data){
        _userData = data;
    }

    public UserData getUserData(){
        return _userData;
    }

    public void setAccessTokenData(AccessTokenData data){
        _accessTokenData = data;
    }

    public AccessTokenData getAccessTokenData(){
        return _accessTokenData;
    }
}
