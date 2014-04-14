package com.fantasysport.webaccess.responses;

/**
 * Created by bylynka on 4/14/14.
 */
public class SubmitRosterResponse {

    private String _message;

    public SubmitRosterResponse(String message){
        _message = message;
    }

    public String getMessage(){
        return _message;
    }
}
