package com.fantasysport.repo;

import com.fantasysport.Const;
import com.fantasysport.models.*;
import com.fantasysport.models.fwc.FWCData;
import com.fantasysport.models.nonfantasy.NFData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bylynka on 2/14/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Storage implements Serializable {

    private static Storage _instance;
    private NFData _nfData;
    private FWCData _fwcData;

    private Storage() {}

    @JsonProperty("user_data")
    private UserData _userData;

    @JsonProperty("markets_container")
    private MarketsContainer _marketsContainer;

    @JsonProperty("default_roster_data")
    private DefaultRosterData _defaultRosterData;

    public static Storage instance() {
        if (_instance == null) {
            synchronized (Storage.class) {
                if (_instance == null) {
                    _instance = new Storage();
                }
            }
        }
        return _instance;
    }

    public void setDefaultRosterData(DefaultRosterData data){
        _defaultRosterData = data;
    }

    public DefaultRosterData getDefaultRosterData(){
        return _defaultRosterData;
    }

    public void setMarketsContainer(MarketsContainer container){
        _marketsContainer = container;
    }

    public MarketsContainer getMarketsContainer(){
        return _marketsContainer;
    }

    public List<Market> getMarkets(){
        return _marketsContainer!= null? _marketsContainer.getMarkets(): null;
    }

    public void setUserData(UserData userData){
        _userData = userData;
    }

    public UserData getUserData(){
        return _userData;
    }

    public int getCategoryType(){
                if(_userData.getCategory().equalsIgnoreCase("sports")){
                    return _userData.getSport().equalsIgnoreCase("fwc")
                           ? Const.FWC
                            : Const.NON_FANTASY_SPORT;
                }else{
                    return Const.FANTASY_SPORT;
                }
    }

    public void setNFData(NFData nfData) {
        _nfData = nfData;
    }

    public NFData getNFDataContainer(){
        return _nfData;
    }

    public void setFWCData(FWCData FWCData) {
        _fwcData = FWCData;
    }

    public FWCData getFWCData() {
        return _fwcData;
    }
}
