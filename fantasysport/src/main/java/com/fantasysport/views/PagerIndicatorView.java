package com.fantasysport.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.fantasysport.R;

/**
 * Created by bylynka on 6/2/14.
 */
public class PagerIndicatorView extends LinearLayout {

    public PagerIndicatorView(Context context) {
        super(context);
        init();
    }

    public PagerIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setOrientation(HORIZONTAL);
    }

    public void setPageAmount(int amount){
        removeAllViews();
        for(int i=0; i < amount; i++){
            ImageView imageView = new ImageView(getContext());
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(params);
            addView(imageView);
        }
        setActivePage(0);
    }

    public void setActivePage(int page){
        for (int i = 0; i < getChildCount(); i++){
            ImageView img = (ImageView)getChildAt(i);
            Drawable drawable = i == page ? getResources().getDrawable(R.drawable.swipe_active) : getResources().getDrawable(R.drawable.swipe_passive);
            img.setBackgroundDrawable(drawable);
        }
    }
}
