package com.fantasysport.utility;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by bylynka on 4/7/14.
 */
public class TypefaceProvider {

    private static Typeface _robotoThin;
    private static Typeface _prohibitionRoundTypeFace;

    public static Typeface getRobotoThin(Context context){
        if(_robotoThin == null){
            _robotoThin  = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Thin.ttf");
        }
        return _robotoThin;
    }

    public static Typeface getProhibitionRound(Context context){
        if(_prohibitionRoundTypeFace == null){
            _prohibitionRoundTypeFace  = Typeface.createFromAsset(context.getAssets(), "fonts/ProhibitionRound.ttf");
        }
        return _prohibitionRoundTypeFace;
    }
}
