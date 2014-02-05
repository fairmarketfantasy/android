package com.fantasysport.webaccess.Responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 2/4/14.
 */
public class BaseResponse {

    @SerializedName("error")
    private String _error;

    public String getError(){
        return _error;
    }
}
