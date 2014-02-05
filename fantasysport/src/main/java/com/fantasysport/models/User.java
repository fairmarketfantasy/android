package com.fantasysport.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 2/3/14.
 */
public class User {

    @SerializedName("username")
    private String _login;

    @SerializedName("name")
    private String _realName;

    @SerializedName("email")
    private String _email;

    @SerializedName("password")
    private String _password;

    @SerializedName("password_confirmation")
    private String _passwordConfirmation;

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

    public void setPasswordConfirmation(String passwordConfirmation){
        _passwordConfirmation = passwordConfirmation;
    }

}
