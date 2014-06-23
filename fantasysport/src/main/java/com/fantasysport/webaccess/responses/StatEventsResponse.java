package com.fantasysport.webaccess.responses;

import com.fantasysport.models.StatsItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by bylynka on 3/5/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatEventsResponse {

    @JsonProperty("events")
    private List<StatsItem> _events;

    public void setStatEvents(List<StatsItem> events){
        _events = events;
    }

    public List<StatsItem> getStatEvents(){
        return _events;
    }
}
