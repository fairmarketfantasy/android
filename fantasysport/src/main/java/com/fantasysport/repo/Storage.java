package com.fantasysport.repo;

import com.fantasysport.models.DefaultRosterData;
import com.fantasysport.models.Market;
import com.fantasysport.models.UserData;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bylynka on 2/14/14.
 */
public class Storage {

    private static Storage _instance;

    private Storage() {}

    @SerializedName("user_data")
    private UserData _userData;

    @SerializedName("markets")
    private List<Market> _markets;

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

    public void setMarkets(List<Market> markets){
        _markets = markets;
    }

    public List<Market> getMarkets(){
        return _markets;
    }

    public void setUserData(UserData userData){
        _userData = userData;
    }

    public UserData getUserData(){
        return _userData;
    }

}
