package com.fantasysport;

import android.app.Application;
import com.fantasysport.models.AccessTokenData;
import com.fantasysport.models.DefaultRosterData;
import com.fantasysport.models.MarketsContainer;
import com.fantasysport.models.UserData;
import com.fantasysport.repo.Storage;
import com.fantasysport.utility.CacheProvider;
import com.fantasysport.webaccess.RequestHelper;
import com.google.gson.Gson;

/**
 * Created by bylynka on 2/4/14.
 */
public class App extends Application {

    private static App _current;
    private final String ACCESS_TOKEN = "access_token";
    private final String MARKETS = "markets";
    private final String DEFAULT_ROSTER_DATA = "default_roster_data";
    private final String USER_DATA = "user_data";

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

        dataInStr = CacheProvider.getString(this, USER_DATA);
        if(dataInStr != null){
            UserData userData  = gson.fromJson(dataInStr, UserData.class);
            storage.setUserData(userData);
        }

        dataInStr = CacheProvider.getString(this, MARKETS);
        if(dataInStr != null){
            MarketsContainer marketsData  = gson.fromJson(dataInStr, MarketsContainer.class);
            storage.setMarketsContainer(marketsData);
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
            CacheProvider.putString(App.this, ACCESS_TOKEN, null);
            CacheProvider.putString(App.this, MARKETS, null);
            CacheProvider.putString(App.this, DEFAULT_ROSTER_DATA, null);
            CacheProvider.putString(App.this, USER_DATA, null);
        }

        @Override
        public void onMarkets(MarketsContainer container) {
            String marketsStr = container != null? new Gson().toJson(container): null;
            CacheProvider.putString(App.this, MARKETS, marketsStr);
            CacheProvider.putBoolean(App.this, Const.TIME_ZONE_CHANGED ,false);
        }

        @Override
        public void onDefaultRosterData(DefaultRosterData data) {
            String dataStr = data != null? new Gson().toJson(data): null;
            CacheProvider.putString(App.this, DEFAULT_ROSTER_DATA, dataStr);
        }

        @Override
        public void onUserData(UserData data) {
            String dataStr = data != null? new Gson().toJson(data): null;
            CacheProvider.putString(App.this, USER_DATA, dataStr);
        }
    };

}
