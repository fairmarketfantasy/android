package com.fantasysport.webaccess.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 5/26/14.
 */
public class MsgResponse {

    @SerializedName("msg")
    private String _msg;

    public String getMessage(){
        return _msg;
    }
}
