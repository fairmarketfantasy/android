package com.fantasysport.webaccess.Requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 2/13/14.
 */
public class AccessTokenRequestBody {

    public final static String PASSWORD = "password";
    public final static String FACEBOOK = "facebook";

    @SerializedName("client_id")
    private String _clientId = "fairmarketfantasy";

    @SerializedName("client_secret")
    private String _clientSecret = "f4n7Astic";

    @SerializedName("grant_type")
    private String _grantType = "password";

    @SerializedName("username")
    private String _email;

    @SerializedName("password")
    private String _password;

    @SerializedName("token")
    private String _accessToken;

    @SerializedName("uid")
    private String _uid;

    public AccessTokenRequestBody(String email, String password){
        _email = email;
        _password = password;
        _grantType = PASSWORD;
    }

    public AccessTokenRequestBody(){
    }

    public void setFacebookAuth(String accessToken, String uid){
        _grantType = FACEBOOK;
        _uid = uid;
        _accessToken = accessToken;
    }
}
