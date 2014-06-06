package com.fantasysport.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.fantasysport.R;
import com.fantasysport.utility.TypefaceProvider;

/**
 * Created by bylynka on 3/20/14.
 */
public class PredictionsDropdownAdapter implements SpinnerAdapter {

    private Context _context;
    private int _dropDownItemHeight;
    private String _rosterHeader;
    private String _individualHeader;

    public PredictionsDropdownAdapter(Context context, int dropDownItemHeight) {
        _context = context;
        _dropDownItemHeight = dropDownItemHeight;
    }

    public void setRosterHeader(String header){
        _rosterHeader = header;
    }

    public void setIndividualHeader(String header){
        _individualHeader = header;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        String text = position == 0? _rosterHeader: _individualHeader;
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
        lbl.setTypeface(TypefaceProvider.getProhibitionRound(_context));
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
        return position == 0? _rosterHeader : _individualHeader;
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
        lbl.setTypeface(TypefaceProvider.getProhibitionRound(_context));
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
