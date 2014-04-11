package com.fantasysport.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;
import com.fantasysport.R;
import com.fantasysport.utility.TypefaceProvider;

/**
 * Created by bylynka on 4/11/14.
 */
public class ExButton extends Button {

    public ExButton(Context context) {
        super(context);
    }

    public ExButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ExTextView,
                0, 0);
        try {
            switch (a.getInteger(R.styleable.ExTextView_fontFamily, 0)) {
                case 1:
                    setTypeface(TypefaceProvider.getProhibitionRound(context));
                    break;
                case 2:
                    setTypeface(TypefaceProvider.getRobotoThin(context));
                    break;
            }
        } finally {
            a.recycle();
        }
    }
}
