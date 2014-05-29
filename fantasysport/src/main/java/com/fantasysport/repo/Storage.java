package com.fantasysport.repo;

import com.fantasysport.models.*;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bylynka on 2/14/14.
 */
public class Storage implements Serializable {

    private static Storage _instance;
    private NFData _nfData;

    private Storage() {}

    @SerializedName("user_data")
    private UserData _userData;

    @SerializedName("markets_container")
    private MarketsContainer _marketsContainer;

    @SerializedName("default_roster_data")
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

    public boolean isFantasyCategory() {
        return _userData.getCategory().equalsIgnoreCase("fantasy_sports") ;
    }

    public void setNFData(NFData nfData) {
        _nfData = nfData;
    }

    public NFData getNFDataContainer(){
        return _nfData;
    }
}
