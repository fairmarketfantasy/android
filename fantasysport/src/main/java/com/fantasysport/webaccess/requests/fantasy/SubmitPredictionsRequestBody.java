package com.fantasysport.webaccess.requests.fantasy;

import com.fantasysport.models.StatsItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by bylynka on 3/6/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmitPredictionsRequestBody {

    @JsonProperty("roster_id")
    private int _rosterId;

    @JsonProperty("market_id")
    private int _marketId;

    @JsonProperty("player_id")
    private String _statsId;

    @JsonProperty("events")
    private List<StatsItem> _events;

    public SubmitPredictionsRequestBody(int rosterId, int marketId, String statsId, List<StatsItem> events){
        _rosterId = rosterId;
        _marketId = marketId;
        _statsId = statsId;
        _events = events;
    }

}
