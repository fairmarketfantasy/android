package com.fantasysport.webaccess.requests;

import com.fantasysport.models.User;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 2/4/14.
 */
public class UserRequestBody {

    @SerializedName("user")
    public User _user;

    public UserRequestBody(User user){
        _user = user;
    }

    public User getUser(){
        return _user;
    }

}
