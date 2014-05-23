package com.fantasysport.webaccess.requests.fantasy;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 2/26/14.
 */
public class CreateRosterRequestBody {

    @SerializedName("market_id")
    private int _marketId;

    public CreateRosterRequestBody(int marketId){
        _marketId = marketId;
    }
}
