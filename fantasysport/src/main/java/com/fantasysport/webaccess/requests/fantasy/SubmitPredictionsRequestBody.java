package com.fantasysport.webaccess.requests.fantasy;

import com.fantasysport.models.StatsItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bylynka on 3/6/14.
 */
public class SubmitPredictionsRequestBody {

    @SerializedName("roster_id")
    private int _rosterId;

    @SerializedName("market_id")
    private int _marketId;

    @SerializedName("player_id")
    private String _statsId;

    @SerializedName("events")
    private List<StatsItem> _events;

    public SubmitPredictionsRequestBody(int rosterId, int marketId, String statsId, List<StatsItem> events){
        _rosterId = rosterId;
        _marketId = marketId;
        _statsId = statsId;
        _events = events;
    }

}
