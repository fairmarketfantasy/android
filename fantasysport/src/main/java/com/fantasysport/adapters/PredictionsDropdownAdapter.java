package com.fantasysport.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.fantasysport.R;

/**
 * Created by bylynka on 3/20/14.
 */
public class PredictionsDropdownAdapter implements SpinnerAdapter {

    private Context _context;
    private int _dropDownItemHeight;
    private Typeface _typeFace;

    public PredictionsDropdownAdapter(Context context, int dropDownItemHeight, Typeface typeface) {
        _context = context;
        _dropDownItemHeight = dropDownItemHeight;
        _typeFace = typeface;
    }

//    public View setItem(int position, View convertView, ViewGroup parent){
//        String text = position == 0 ? "Roster Predictions" : "Individual Predictions";
//        if (convertView == null) {
//            LayoutInflater mInflater = (LayoutInflater)
//                    _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//            convertView = mInflater.inflate(android.R.layout.simple_spinner_item, parent, false);
//            Drawable background = _context.getResources().getDrawable(R.color.action_bar_background);
//            convertView.setBackgroundDrawable(background);
//        }
//        TextView lbl = (TextView) convertView.findViewById(android.R.id.text1);
//        lbl.setText(text);
//        lbl.setHeight(_dropDownItemHeight);
//        lbl.setGravity(Gravity.CENTER);
//        lbl.setTextColor(Color.WHITE);
//        lbl.setTypeface(_typeFace);
//        return convertView;
//    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        int stringId = position == 0? R.string.roster_predictions: R.string.individual_predictions;
        String text = _context.getString(stringId);
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(android.R.layout.simple_spinner_item, parent, false);
            Drawable background = _context.getResources().getDrawable(R.color.action_bar_background);
            convertView.setBackgroundDrawable(background);
        }
        TextView lbl = (TextView) convertView.findViewById(android.R.id.text1);
        lbl.setText(text);
        lbl.setHeight(_dropDownItemHeight);
        lbl.setTextColor(Color.WHITE);
        lbl.setTypeface(_typeFace);
        lbl.setGravity(Gravity.CENTER_VERTICAL);
        lbl.setPadding(_context.getResources().getDimensionPixelOffset(R.dimen.dropdown_item_padding),0,0,0);
        lbl.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        return convertView;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        int stringId = position == 0? R.string.roster_predictions: R.string.individual_predictions;
        return _context.getString(stringId);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int stringId = position == 0? R.string.roster_predictions: R.string.individual_predictions;
        String text = _context.getString(stringId);
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        TextView lbl = (TextView) convertView.findViewById(android.R.id.text1);
        lbl.setText(text);
        lbl.setTextColor(Color.WHITE);
        lbl.setTypeface(_typeFace);
        lbl.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
