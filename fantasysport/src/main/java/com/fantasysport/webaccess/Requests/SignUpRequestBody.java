package com.fantasysport.webaccess.Requests;

import com.fantasysport.models.User;
import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 2/4/14.
 */
public class SignUpRequestBody {

    @SerializedName("user")
    private User _user;

    public SignUpRequestBody(User user){
        _user = user;
    }

}
