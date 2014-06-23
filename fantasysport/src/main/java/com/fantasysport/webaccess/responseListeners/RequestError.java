package com.fantasysport.webaccess.responseListeners;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by bylynka on 2/4/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestError {

    @JsonProperty("error")
    private String _message;

    @JsonProperty("error_sbdescription")
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
