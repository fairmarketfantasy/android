package com.fantasysport.webaccess.responses;

import com.fantasysport.models.DefaultRosterData;
import com.fantasysport.models.Market;
import com.fantasysport.models.MarketsContainer;

import java.util.List;

/**
 * Created by bylynka on 2/26/14.
 */
public class MarketResponse {

    private MarketsContainer _marketsContainer;
    private DefaultRosterData _rosterData;

    public MarketResponse(MarketsContainer container, DefaultRosterData defaultRosterData){
        _marketsContainer = container;
        _rosterData = defaultRosterData;
    }

    public MarketsContainer getMarketsContainer(){
        return _marketsContainer;
    }

    public DefaultRosterData getDefaultRosterData(){
        return  _rosterData;
    }

}
