package com.fantasysport.views;

import android.content.Context;
import android.graphics.Canvas;

import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by bylynka on 2/17/14.
 */
public class RoundedImageView extends ImageView {

    private int _radius=100;

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedImageView(Context context) {
        super(context);
    }

    public void setRadius(int radius){
        _radius = radius;
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        Path clipPath = new Path();
        int w = this.getWidth();
        int h = this.getHeight();
//        clipPath.addCircle(1f, 1f, 1f, 1f, null);
        clipPath.addRoundRect(new RectF(0,0,w,h), _radius, _radius, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }


}
