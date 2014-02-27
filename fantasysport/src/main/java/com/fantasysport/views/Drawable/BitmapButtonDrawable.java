package com.fantasysport.views.Drawable;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by bylynka on 2/24/14.
 */
public class BitmapButtonDrawable extends StateListDrawable {

    private Bitmap _bitmap;
    private int _normalState;
    private int _pressedState;
    private Paint _paint;

    public BitmapButtonDrawable(Bitmap bitmap, int normalStateColor, int pressedStateColor){
        _bitmap = bitmap;
        _normalState = normalStateColor;
        _pressedState = pressedStateColor;
        _paint = new Paint();
        addState(new int[] {android.R.attr.state_enabled, -android.R.attr.state_pressed}, _normalStateDrawable);
        addState(new int[] {android.R.attr.state_pressed}, _pressedStateDrawable);
    }

    private Drawable _normalStateDrawable = new Drawable() {
        @Override
        public void draw(Canvas canvas) {
            ColorMatrix cm = new ColorMatrix();

            cm.set(new float[] {
                    0, 0f, 0, 0, Color.red(_normalState),
                    0, 0, 0f, 0, Color.green(_normalState),
                    0, 0, 0, 0f, Color.blue(_normalState),
                    0, 0, 0, 1, 0 });
            _paint.setColorFilter(new ColorMatrixColorFilter(cm));
            canvas.drawBitmap(_bitmap, 0, 0, _paint);
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
            ColorMatrix cm = new ColorMatrix();
            cm.set(new float[] {
                    0, 0f, 0, 0, Color.red(_pressedState),
                    0, 0, 0f, 0, Color.green(_pressedState),
                    0, 0, 0, 0f, Color.blue(_pressedState),
                    0, 0, 0, 1, 0 });
            _paint.setColorFilter(new ColorMatrixColorFilter(cm));
            canvas.drawBitmap(_bitmap, 0, 0, _paint);
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
}
