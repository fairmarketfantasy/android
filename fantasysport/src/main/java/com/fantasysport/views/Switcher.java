package com.fantasysport.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.fantasysport.R;

/**
 * Created by bylynka on 2/22/14.
 */
public class Switcher extends FrameLayout implements View.OnClickListener {

    private TextView _lbl;

    public Switcher(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Switcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Switcher(Context context) {
        super(context);
        init();
    }

    private void addButton(){
        _lbl = new TextView(getContext());
        _lbl.setBackgroundResource(R.drawable.switcher_button);
        _lbl.setTextColor(Color.WHITE);
        int width = (int)getResources().getDimension(R.dimen.switcher_btn_width);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, LayoutParams.MATCH_PARENT);
        _lbl.setLayoutParams(params);
        _lbl.setGravity(Gravity.CENTER);
        addView(_lbl);
    }

    private void init(){
       setBackgroundResource(R.drawable.switcher);
       int padding = (int)getResources().getDimension(R.dimen.switcher_padding);
        setPadding(padding, padding, padding, padding);
        addButton();
        setSelected(false);
        setOnClickListener(this);
    }

    @Override
    public void setSelected(boolean selected) {
        _lbl.setSelected(selected);
        int gravity = selected? Gravity.RIGHT: Gravity.LEFT;
        String text = selected? "ON": "OFF";
        int width = (int)getResources().getDimension(R.dimen.switcher_btn_width);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width, LayoutParams.MATCH_PARENT);
        params.gravity = gravity;
        _lbl.setLayoutParams(params);
        _lbl.setText(text);
//        ((LayoutParams)_lbl.getLayoutParams()).gravity = gravity;
//        _lbl.setGravity(gravity);
        super.setSelected(selected);
    }

    @Override
    public void onClick(View v) {
        setSelected(!isSelected());
    }
}
