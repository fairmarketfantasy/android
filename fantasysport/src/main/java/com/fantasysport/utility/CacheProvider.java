package com.fantasysport.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by bylynka on 3/23/14.
 */
public class CacheProvider {

    private static final String APP_PREFERENCES = "mysettings";

    private static SharedPreferences.Editor getEditor(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.edit();
    }

    public static boolean putString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.contains(key) ? preferences.getString(key, null) : null;
    }
}
