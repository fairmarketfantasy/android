package com.fantasysport;

import android.app.Application;
import com.fantasysport.models.*;
import com.fantasysport.models.fwc.FWCData;
import com.fantasysport.models.nonfantasy.NFData;
import com.fantasysport.repo.Storage;
import com.fantasysport.utility.CacheProvider;
import com.fantasysport.webaccess.RequestHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by bylynka on 2/4/14.
 */
public class App extends Application {

    private static App _current;
    private final String ACCESS_TOKEN = "access_token";
    private final String MARKETS = "markets";
    private final String DEFAULT_ROSTER_DATA = "default_roster_data";
    private final String NON_FANTASY_DATA = "non_fantasy_data";
    private final String FWC_DATA = "non_fantasy_data";

    public static App getCurrent(){
        return _current;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _current = this;
        restoreState();
        setRequestHelper();
//        Ubertesters.initialize(this);
    }

    private void setRequestHelper(){
        RequestHelper helper = RequestHelper.instance();
        helper.setListener(_requestHelperListener);
    }

    private void restoreState(){
        Gson gson = new Gson();
        String dataInStr = CacheProvider.getString(this, ACCESS_TOKEN);
        Storage storage = Storage.instance();
        if(dataInStr == null){
            return;
        }
        AccessTokenData data = gson.fromJson(dataInStr, AccessTokenData.class);
        RequestHelper helper = RequestHelper.instance();
        helper.setAccessTokenData(data);

        dataInStr = CacheProvider.getString(this, DEFAULT_ROSTER_DATA);
        if(dataInStr != null){
            DefaultRosterData defaultRosterData  = gson.fromJson(dataInStr, DefaultRosterData.class);
            storage.setDefaultRosterData(defaultRosterData);
        }

        dataInStr = CacheProvider.getString(this, Const.USER_DATA);
        if(dataInStr != null){
            UserData userData  = gson.fromJson(dataInStr, UserData.class);
            storage.setUserData(userData);
        }

        dataInStr = CacheProvider.getString(this, MARKETS);
        if(dataInStr != null){
            MarketsContainer marketsData  = gson.fromJson(dataInStr, MarketsContainer.class);
            storage.setMarketsContainer(marketsData);
        }

        dataInStr = CacheProvider.getString(this, NON_FANTASY_DATA);
        if(dataInStr != null){
            NFData nfData = getGson().fromJson(dataInStr, NFData.class);
            storage.setNFData(nfData);
        }

        dataInStr = CacheProvider.getString(this, FWC_DATA);
        if(dataInStr != null){
            FWCData fwcData = getGson().fromJson(dataInStr, FWCData.class);
            storage.setFWCData(fwcData);
        }
    }

    RequestHelper.IListener _requestHelperListener = new RequestHelper.IListener() {
        @Override
        public void onAccessTokenDataChanged(AccessTokenData data) {
            String accessTokenData = new Gson().toJson(data);
            CacheProvider.putString(App.this, ACCESS_TOKEN, accessTokenData);
        }

        @Override
        public void onSignOut() {
            RequestHelper.instance().setAccessTokenData(null);
            Storage.instance().setUserData(null);
            Storage.instance().setMarketsContainer(null);
            Storage.instance().setDefaultRosterData(null);
            CacheProvider.putString(App.this, ACCESS_TOKEN, null);
            CacheProvider.putString(App.this, MARKETS, null);
            CacheProvider.putString(App.this, DEFAULT_ROSTER_DATA, null);
            CacheProvider.putString(App.this, Const.USER_DATA, null);
            CacheProvider.putString(App.this, FWC_DATA, null);
            CacheProvider.putString(App.this, NON_FANTASY_DATA, null);
        }

        @Override
        public void onMarkets(MarketsContainer container) {
            String marketsStr = container != null? new Gson().toJson(container): null;
            CacheProvider.putString(App.this, MARKETS, marketsStr);
            CacheProvider.putBoolean(App.this, Const.TIME_ZONE_CHANGED, false);
            UserData data = Storage.instance().getUserData();
            if(data != null){
                onUserData(Storage.instance().getUserData());
            }
        }

        @Override
        public void onDefaultRosterData(DefaultRosterData data) {
            String dataStr = data != null? getGson().toJson(data): null;
            CacheProvider.putString(App.this, DEFAULT_ROSTER_DATA, dataStr);
        }

        @Override
        public void onUserData(UserData data) {
            String dataStr = data != null? getGson().toJson(data): null;
            CacheProvider.putString(App.this, Const.USER_DATA, dataStr);
        }

        @Override
        public void onNonFantasyData(NFData data) {
            String dataStr = data != null? getGson().toJson(data): null;
            CacheProvider.putString(App.this, NON_FANTASY_DATA, dataStr);
        }

        @Override
        public void onFWCData(FWCData data) {
            String dataStr = data != null? getGson().toJson(data): null;
            CacheProvider.putString(App.this, FWC_DATA, dataStr);
        }
    };

    private Gson getGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        return gsonBuilder.create();
    }

}
