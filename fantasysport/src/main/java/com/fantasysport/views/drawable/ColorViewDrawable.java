package com.fantasysport.views.drawable;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by bylynka on 2/26/14.
 */
public class ColorViewDrawable extends StateListDrawable {

    private int _normalState;
    private int _pressedState;
    private IStateListener _listener;

    public ColorViewDrawable(int normalStateColor, int pressedStateColor){
        _normalState = normalStateColor;
        _pressedState = pressedStateColor;
        addState(new int[] {android.R.attr.state_enabled, -android.R.attr.state_pressed}, _normalStateDrawable);
        addState(new int[] {android.R.attr.state_pressed}, _pressedStateDrawable);
    }

    public void setStateListener(IStateListener listener){
        _listener = listener;
    }

    private void raiseStateListener(int state){
        if(_listener != null){
            _listener.stateChanged(state);
        }
    }

    private Drawable _normalStateDrawable = new Drawable() {
        @Override
        public void draw(Canvas canvas) {
            raiseStateListener(android.R.attr.state_active);
            canvas.drawColor(_normalState);
        }

        @Override
        public void setAlpha(int alpha) {
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
        }

        @Override
        public int getOpacity() {
            return 255;
        }
    };

    private Drawable _pressedStateDrawable = new Drawable() {
        @Override
        public void draw(Canvas canvas) {
            raiseStateListener(android.R.attr.state_pressed);
            canvas.drawColor(_pressedState);
        }

        @Override
        public void setAlpha(int alpha) {
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
        }

        @Override
        public int getOpacity() {
            return 255;
        }
    };


    public interface IStateListener{
        public void stateChanged(int state);
    }
}
