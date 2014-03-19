package com.fantasysport.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by bylynka on 3/14/14.
 */
public class LockableScrollView extends ScrollView {

    private boolean _scrollable = true;
    private ScrollViewListener _listener;
    private int _y;

    public LockableScrollView(Context context) {
        super(context);
    }

    public LockableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setScrollingEnabled(boolean enabled) {
        _scrollable = enabled;
    }

    public void setListener(ScrollViewListener listener){
        _listener = listener;
    }

    public boolean isScrollable() {
        return _scrollable;
    }

    public int getYPosition(){
        return _y;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        _y = y;
        if(_listener != null) {
            _listener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // if we can scroll pass the event to the superclass
                if (_scrollable) return super.onTouchEvent(ev);
                // only continue to handle the touch event if scrolling enabled
                return _scrollable; // mScrollable is always false at this point
            default:
                return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Don't do anything with intercepted touch events if
        // we are not scrollable
        if (!_scrollable) return false;
        else return super.onInterceptTouchEvent(ev);
    }

    public interface ScrollViewListener {

        public void onScrollChanged(LockableScrollView scrollView, int x, int y, int oldx, int oldy);

    }

}
