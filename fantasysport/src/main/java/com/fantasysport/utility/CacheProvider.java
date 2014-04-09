package com.fantasysport.utility;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by bylynka on 3/23/14.
 */
public class CacheProvider {

    private static final String APP_PREFERENCES = "app_settings";

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

    public static boolean putLong(Context context, String key, long value){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putLong(key, value);
        return editor.commit();
    }

    public static long getLong(Context context, String key){
        SharedPreferences preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.contains(key) ? preferences.getLong(key, -1) : -1;
    }
}
