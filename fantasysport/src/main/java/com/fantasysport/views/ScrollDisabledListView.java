package com.fantasysport.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.fantasysport.R;

/**
 * Created by bylynka on 2/28/14.
 */
public class ScrollDisabledListView extends LinearLayout implements View.OnClickListener {

    private BaseAdapter _adapter;
    private AdapterView.OnItemClickListener _onItemClickListener;
    private int _deviderId = 5001;

    public ScrollDisabledListView(Context context) {
        super(context);
        init();
    }

    public ScrollDisabledListView(Context context, AttributeSet attrs) {
        super(context, attrs);


//        int[] attributes = {android.R.attr.dividerHeight, android.R.attr.divider};
////        R.style.AppIconTheme
//        TypedArray ta = getContext().obtainStyledAttributes(R.style.AppIconTheme, attributes);
        init();
    }

    private void init(){
        setOrientation(LinearLayout.VERTICAL);
    }


    public void setAdapter(BaseAdapter adapter){
        _adapter = adapter;
        _adapter.registerDataSetObserver(_dataObserver);
    }

    private void updateData(){
        removeAllViews();
        if(_adapter == null){
            return;
        }
        int count = _adapter.getCount();
        Context context = getContext();
        for (int i = 0; i < count; i++){

            View vi = _adapter.getView(i, null, null);
            vi.setOnClickListener(this);
            addView(vi);
            LayoutInflater mInflater = (android.view.LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View devider = mInflater.inflate(R.layout.players_divider, null);
            devider.setId(_deviderId);
            addView(devider);
        }
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener){
        _onItemClickListener = listener;

    }

    DataSetObserver _dataObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            updateData();
        }
    };

    @Override
    public void onClick(View v) {
        if(_onItemClickListener == null){
            return;
        }
        int count = getChildCount();
        int index = -1;
        for (int i = 0; i < count; i++){
            View child = getChildAt(i);
            if(child.getId() == _deviderId){
                continue;
            }
            index++;
            if(v == child){
                _onItemClickListener.onItemClick(null, v, index, index);
            }
        }

    }
}