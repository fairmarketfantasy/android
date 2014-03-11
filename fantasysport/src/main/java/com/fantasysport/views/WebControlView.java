package com.fantasysport.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

/**
 * Created by bylynka on 3/7/14.
 */
public class WebControlView extends WebView {
    public WebControlView(Context context) {
        super(context);
        init();
    }

    public WebControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WebControlView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        getSettings().setBuiltInZoomControls(false);
        getSettings().setSupportZoom(false);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setUseWideViewPort(true);
        setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setAppCacheEnabled(true);

        requestFocus(View.FOCUS_DOWN);
        setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });
    }
}
