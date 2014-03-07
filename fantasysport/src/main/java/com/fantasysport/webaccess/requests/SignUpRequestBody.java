package com.fantasysport.webaccess.requests;

import com.fantasysport.models.User;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 2/4/14.
 */
public class SignUpRequestBody {

    @SerializedName("user")
    public User _user;

    public SignUpRequestBody(User user){
        _user = user;
    }

    public User getUser(){
        return _user;
    }

}
