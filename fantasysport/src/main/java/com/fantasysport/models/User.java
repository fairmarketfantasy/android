package com.fantasysport.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by bylynka on 2/3/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @JsonProperty("username")
    private String _login;

    @JsonProperty("name")
    private String _realName;

    @JsonProperty("email")
    private String _email;

    @JsonProperty("password")
    private String _password;

    @JsonProperty("password_confirmation")
    private String _passwordConfirmation;

    @JsonProperty("current_password")
    private String _currentPassword;

    @JsonProperty("id")
    private int _id;

    @JsonProperty("avatar")
    private Avatar _avatar;

    public void setAvatar(Avatar avatar){
        _avatar = avatar;
    }

    public String getLogin(){
        return  _login;
    }

    public void setLogin(String login){
        _login = login;
    }

    public String getRealName(){
        return _realName;
    }

    public void setRealName(String realName){
        _realName = realName;
    }

    public String getEmail(){
        return _email;
    }

    public void setEmail(String email){
        _email = email;
    }

    public void setPassword(String password){
        _password = password;
    }

    public String getPassword(){
        return _password;
    }

    public void setPasswordConfirmation(String passwordConfirmation){
        _passwordConfirmation = passwordConfirmation;
    }

    public void setCurrentPassword(String currentPassword){
        _currentPassword = currentPassword;
    }

    public void setId(int id){
        _id = id;
    }

}
