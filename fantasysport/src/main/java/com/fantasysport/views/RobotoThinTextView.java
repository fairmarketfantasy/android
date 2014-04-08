package com.fantasysport.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.fantasysport.utility.TypefaceProvider;

/**
 * Created by bylynka on 4/7/14.
 */
public class RobotoThinTextView extends TextView {

    public RobotoThinTextView(Context context) {
        super(context);
        init();
    }

    public RobotoThinTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RobotoThinTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        setTypeface(TypefaceProvider.getRobotoThin(getContext()));
    }
}
