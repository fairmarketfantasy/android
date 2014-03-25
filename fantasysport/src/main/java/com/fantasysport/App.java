package com.fantasysport;

import android.app.Application;
import android.util.Log;
import com.fantasysport.models.AccessTokenData;
import com.fantasysport.utility.CacheProvider;
import com.fantasysport.webaccess.RequestHelper;
import com.google.gson.Gson;
import com.ubertesters.sdk.Ubertesters;

/**
 * Created by bylynka on 2/4/14.
 */
public class App extends Application {

    private static App _current;
    private final String ACCESS_TOKEN = "access_token";

    public static App getCurrent(){
        return _current;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _current = this;
        restoreAccessToken();
        setRequestHelper();
//        Ubertesters.initialize(this);
    }

    private void setRequestHelper(){
        RequestHelper helper = RequestHelper.instance();
        helper.setListener(_requestHelperListener);
    }

    private void restoreAccessToken(){
        String accessTokenStringData = CacheProvider.getString(this, ACCESS_TOKEN);
        AccessTokenData data = new Gson().fromJson(accessTokenStringData, AccessTokenData.class);
        RequestHelper helper = RequestHelper.instance();
        helper.setAccessTokenData(data);

    }

    RequestHelper.IListener _requestHelperListener = new RequestHelper.IListener() {
        @Override
        public void onAccessTokenDataChanged(AccessTokenData data) {
            String accessTokenData = new Gson().toJson(data);
            CacheProvider.putString(App.this, ACCESS_TOKEN, accessTokenData);
        }
    };

}
