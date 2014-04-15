package com.fantasysport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.fantasysport.utility.CacheProvider;

/**
 * Created by bylynka on 4/15/14.
 */
public class TimeChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        CacheProvider.putBoolean(context, Const.TIME_ZONE_CHANGED ,true);
        Log.i("TimeChangedReceiver", "TIME ZONE CHANGED");
    }

}