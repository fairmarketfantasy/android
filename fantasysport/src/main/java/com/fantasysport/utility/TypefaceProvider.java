package com.fantasysport.utility;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by bylynka on 4/7/14.
 */
public class TypefaceProvider {

    protected static Typeface _robotoThin;

    public static Typeface getRobotoThin(Context context){
        if(_robotoThin == null){
            _robotoThin  = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Thin.ttf");
        }
        return _robotoThin;
    }
}
