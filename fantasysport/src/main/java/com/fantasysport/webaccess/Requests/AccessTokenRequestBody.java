package com.fantasysport.webaccess.Requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 2/13/14.
 */
public class AccessTokenRequestBody {

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


    public AccessTokenRequestBody(String email, String password){
        _email = email;
        _password = password;
    }
}
