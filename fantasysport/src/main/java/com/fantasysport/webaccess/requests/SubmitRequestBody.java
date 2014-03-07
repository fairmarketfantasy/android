package com.fantasysport.webaccess.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 3/4/14.
 */
public class SubmitRequestBody {

    @SerializedName("contest_type")
    private String _contestType;

    public SubmitRequestBody(String contestType){
        _contestType = contestType;
    }
}
