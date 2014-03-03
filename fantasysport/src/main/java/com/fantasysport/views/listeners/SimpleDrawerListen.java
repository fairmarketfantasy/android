package com.fantasysport.views.listeners;

import android.support.v4.widget.DrawerLayout;
import android.view.View;

/**
 * Created by bylynka on 2/27/14.
 */
public abstract class SimpleDrawerListen implements DrawerLayout.DrawerListener {
    @Override
    public void onDrawerSlide(View view, float v) {

    }

    @Override
    public void onDrawerOpened(View view) {
        onStateChanged(true);
    }

    @Override
    public void onDrawerClosed(View view) {
        onStateChanged(false);
    }

    @Override
    public void onDrawerStateChanged(int i) {

    }

    public abstract void onStateChanged(boolean isOpened);
}
