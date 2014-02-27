package com.fantasysport;

import android.app.Application;
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
        Ubertesters.initialize(this);
    }

}
