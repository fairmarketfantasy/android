package com.fantasysport.webaccess.responses;

import com.fantasysport.models.StatsItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bylynka on 3/5/14.
 */
public class StatEventsResponse {

    @SerializedName("events")
    private List<StatsItem> _events;

    public void setStatEvents(List<StatsItem> events){
        _events = events;
    }

    public List<StatsItem> getStatEvents(){
        return _events;
    }
}
