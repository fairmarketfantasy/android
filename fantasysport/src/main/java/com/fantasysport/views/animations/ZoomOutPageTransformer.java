package com.fantasysport.views.animations;

import android.view.View;
import com.fantasysport.views.AnimatedViewPager;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by bylynka on 3/17/14.
 */
public class ZoomOutPageTransformer implements AnimatedViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        if (position < -1) {
            ViewHelper.setAlpha(view, 0);
        } else if (position <= 1) {
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                ViewHelper.setTranslationX(view, horzMargin - vertMargin / 2);
            } else {
                ViewHelper.setTranslationX(view, -horzMargin + vertMargin / 2);
            }
            ViewHelper.setScaleX(view, scaleFactor);
            ViewHelper.setScaleY(view, scaleFactor);

            ViewHelper.setAlpha(view, MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                            (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else { // (1,+Infinity]
            ViewHelper.setAlpha(view, 0);
        }
    }
}