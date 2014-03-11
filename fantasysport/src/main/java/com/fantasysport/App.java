package com.fantasysport;

import android.app.Application;
import com.fantasysport.models.AccessTokenData;
import com.fantasysport.webaccess.RequestHelper;
import com.ubertesters.sdk.Ubertesters;

/**
 * Created by bylynka on 2/4/14.
 */
public class App extends Application {

    private static App _current;

    public static App getCurrent(){
        return _current;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _current = this;
//        Ubertesters.initialize(this);
    }

    private void setRequestHelper(){
        RequestHelper helper = RequestHelper.instance();
        helper.setListener(_requestHelperListener);
    }

    RequestHelper.IListener _requestHelperListener = new RequestHelper.IListener() {
        @Override
        public void onAccessTokenDataChanged(AccessTokenData data) {

        }
    };

}
