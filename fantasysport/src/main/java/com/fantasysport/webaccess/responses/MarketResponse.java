package com.fantasysport.webaccess.responses;

import com.fantasysport.models.DefaultRosterData;
import com.fantasysport.models.Market;

import java.util.List;

/**
 * Created by bylynka on 2/26/14.
 */
public class MarketResponse {

    private List<Market> _markets;
    private DefaultRosterData _rosterData;

    public MarketResponse(List<Market> markets, DefaultRosterData defaultRosterData){
        _markets = markets;
        _rosterData = defaultRosterData;
    }

    public List<Market> getMarkets(){
        return _markets;
    }

    public DefaultRosterData getDefaultRosterData(){
        return  _rosterData;
    }

}
