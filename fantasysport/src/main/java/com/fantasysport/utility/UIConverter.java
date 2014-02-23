package com.fantasysport.utility;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by bylynka on 2/13/14.
 */
public class UIConverter {

    public static int dpiToPixel(float dpi, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpi, getMetric(context));
    }

    public static DisplayMetrics getMetric(Context context) {
        return context.getResources().getDisplayMetrics();
    }
}


