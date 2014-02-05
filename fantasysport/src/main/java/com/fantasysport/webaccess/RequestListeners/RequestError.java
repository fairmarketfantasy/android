package com.fantasysport.webaccess.RequestListeners;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 2/4/14.
 */
public class RequestError {

    @SerializedName("error")
    private String _message;

    public RequestError(){
    }

    public RequestError(String message){
        _message = message;
    }

    public String getMessage(){
        return _message;
    }

}
