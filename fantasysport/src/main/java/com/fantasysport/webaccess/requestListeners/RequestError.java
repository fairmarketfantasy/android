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

    private boolean _isCanceled = false;

    public RequestError(){
    }

    public RequestError(String message){
        _message = message;
    }

    public void setIsCanceledRequest(boolean isCanceled){
        _isCanceled = isCanceled;
    }

    public boolean isCanceledRequest(){
        return _isCanceled;
    }

    public String getMessage()
    {
        if(_message != null && _message.equalsIgnoreCase("invalid_grant")){
            _message = "Invalid username or password";
        }
        return _message;
    }

}
