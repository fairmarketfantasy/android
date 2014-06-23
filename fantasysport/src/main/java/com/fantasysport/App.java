package com.fantasysport;

import android.app.Application;
import com.fantasysport.models.AccessTokenData;
import com.fantasysport.models.DefaultRosterData;
import com.fantasysport.models.MarketsContainer;
import com.fantasysport.models.UserData;
import com.fantasysport.models.fwc.FWCData;
import com.fantasysport.models.nonfantasy.NFData;
import com.fantasysport.repo.Storage;
import com.fantasysport.utility.CacheProvider;
import com.fantasysport.webaccess.RequestHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by bylynka on 2/4/14.
 */
public class App extends Application {

    private static App _current;
    private final String ACCESS_TOKEN = "access_token";
    private final String MARKETS = "markets";
    private final String DEFAULT_ROSTER_DATA = "default_roster_data";
    private final String NON_FANTASY_DATA = "non_fantasy_data";

    public static App getCurrent() {
        return _current;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _current = this;
        try {
            restoreState();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setRequestHelper();
//        Ubertesters.initialize(this);
    }

    private void setRequestHelper() {
        RequestHelper helper = RequestHelper.instance();
        helper.setListener(_requestHelperListener);
    }

    private void restoreState() throws IOException {
        ObjectMapper objectMapper = getObjectMapper();
        String dataInStr = CacheProvider.getString(this, ACCESS_TOKEN);
        Storage storage = Storage.instance();
        if (dataInStr == null) {
            return;
        }
        AccessTokenData data = objectMapper.readValue(dataInStr, AccessTokenData.class);
        RequestHelper helper = RequestHelper.instance();
        helper.setAccessTokenData(data);

        dataInStr = CacheProvider.getString(this, DEFAULT_ROSTER_DATA);
        if (dataInStr != null) {
            DefaultRosterData defaultRosterData = objectMapper.readValue(dataInStr, DefaultRosterData.class);
            storage.setDefaultRosterData(defaultRosterData);
        }

        dataInStr = CacheProvider.getString(this, Const.USER_DATA);
        if (dataInStr != null) {
            UserData userData = objectMapper.readValue(dataInStr, UserData.class);
            storage.setUserData(userData);
        }

        dataInStr = CacheProvider.getString(this, MARKETS);
        if (dataInStr != null) {
            MarketsContainer marketsData = objectMapper.readValue(dataInStr, MarketsContainer.class);
            storage.setMarketsContainer(marketsData);
        }

        dataInStr = CacheProvider.getString(this, NON_FANTASY_DATA);
        if (dataInStr != null) {
            NFData nfData = getObjectMapper().readValue(dataInStr, NFData.class);
            storage.setNFData(nfData);
        }

        dataInStr = CacheProvider.getString(this, Const.FWC_DATA);
        if (dataInStr != null) {
            FWCData fwcData = getObjectMapper().readValue(dataInStr, FWCData.class);
            storage.setFWCData(fwcData);
        }
    }

    RequestHelper.IListener _requestHelperListener = new RequestHelper.IListener() {
        @Override
        public void onAccessTokenDataChanged(AccessTokenData data) {
            try {
                String accessTokenData = getObjectMapper().writeValueAsString(data);
                CacheProvider.putString(App.this, ACCESS_TOKEN, accessTokenData);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
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
            CacheProvider.putString(App.this, Const.FWC_DATA, null);
            CacheProvider.putString(App.this, NON_FANTASY_DATA, null);
        }

        @Override
        public void onMarkets(MarketsContainer container) {
            String marketsStr = null;
            try {
                marketsStr = container != null ? getObjectMapper().writeValueAsString(container) : null;
                CacheProvider.putString(App.this, MARKETS, marketsStr);
                CacheProvider.putBoolean(App.this, Const.TIME_ZONE_CHANGED, false);
                UserData data = Storage.instance().getUserData();
                if (data != null) {
                    onUserData(Storage.instance().getUserData());
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDefaultRosterData(DefaultRosterData data) {
            try {
                String dataStr = data != null ? getObjectMapper().writeValueAsString(data) : null;
                CacheProvider.putString(App.this, DEFAULT_ROSTER_DATA, dataStr);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUserData(UserData data) {
            try {
                String dataStr = data != null ? getObjectMapper().writeValueAsString(data) : null;
                CacheProvider.putString(App.this, Const.USER_DATA, dataStr);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onNonFantasyData(NFData data) {
            try {
                String dataStr = data != null ? getObjectMapper().writeValueAsString(data) : null;
                CacheProvider.putString(App.this, NON_FANTASY_DATA, dataStr);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFWCData(FWCData data) {
            try {
                String dataStr = data != null ? getObjectMapper().writeValueAsString(data) : null;
                CacheProvider.putString(App.this, Const.FWC_DATA, dataStr);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    };

    private ObjectMapper getObjectMapper() {
        ObjectMapper gsonBuilder = new ObjectMapper();
        return gsonBuilder;
    }

}
