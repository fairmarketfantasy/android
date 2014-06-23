package com.fantasysport.webaccess.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by bylynka on 2/13/14.
 */
public class AccessTokenRequestBody {

    public final static String PASSWORD = "password";
    public final static String FACEBOOK = "facebook";
    public final static String REFRESH_TOKEN = "refresh_token";

    @JsonProperty("client_id")
    private String _clientId = "fairmarketfantasy";

    @JsonProperty("client_secret")
    private String _clientSecret = "f4n7Astic";

    @JsonProperty("grant_type")
    private String _grantType = "password";

    @JsonProperty("username")
    private String _email;

    @JsonProperty("password")
    private String _password;

    @JsonProperty("token")
    private String _accessToken;

    @JsonProperty("uid")
    private String _uid;

    @JsonProperty("refresh_token")
    private String _refreshToken;

    public AccessTokenRequestBody(String email, String password) {
        _email = email;
        _password = password;
        _grantType = PASSWORD;
    }

    public AccessTokenRequestBody() {
    }

    public void setFacebookAuth(String accessToken, String uid) {
        _grantType = FACEBOOK;
        _uid = uid;
        _accessToken = accessToken;
    }

    public void setRefreshToken(String refreshToken) {
        _grantType = REFRESH_TOKEN;
        _refreshToken = refreshToken;
    }
}
