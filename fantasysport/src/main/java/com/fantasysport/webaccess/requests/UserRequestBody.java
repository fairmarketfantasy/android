package com.fantasysport.webaccess.requests;

import com.fantasysport.models.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by bylynka on 2/4/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequestBody {

    @JsonProperty("user")
    public User _user;

    public UserRequestBody(User user){
        _user = user;
    }

    public User getUser(){
        return _user;
    }

}
