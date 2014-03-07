package com.fantasysport.webaccess.requestListeners;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 2/4/14.
 */
public class RequestError {

    @SerializedName("error")
    private String _message;

    @SerializedName("error_sbdescription")
    private String _description;

    public RequestError(){
    }

    public RequestError(String message){
        _message = message;
    }

    public String getMessage(){
//        if(_description == null || _description.length() == 0){
//            return _message;
//        }
        return _message;
    }

}
