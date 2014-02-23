package com.fantasysport.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.fantasysport.R;
import com.fantasysport.views.MenuItem;

import java.util.List;

/**
 * Created by bylynka on 2/18/14.
 */
public class MenuListAdapter extends BaseAdapter {

    private List<MenuItem> _items;
    private Context _context;

    public MenuListAdapter(List<MenuItem> items, Context context){
        _items = items;
        _context = context;
    }

    @Override
    public int getCount() {
        return _items.size();
    }

    @Override
    public Object getItem(int position) {
        return _items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.menu_item, null);
        }
        MenuItem menuItem = _items.get(position);
        TextView itemTxt = (TextView)convertView.findViewById(R.id.item);
        itemTxt.setText(menuItem.getTitle());
        return convertView;
    }
}
