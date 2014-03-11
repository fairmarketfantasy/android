package com.fantasysport.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.fantasysport.R;
import com.fantasysport.models.UserData;
import com.fantasysport.utility.image.ImageLoader;

import java.util.List;

/**
 * Created by bylynka on 3/11/14.
 */
public class SettinsAdapter extends BaseAdapter {

    private Context _context;
    private UserData _userData;
    private List<SettingsItemEnum> _items;

    public SettinsAdapter(Context context, UserData userData, List<SettingsItemEnum> items){
        _context = context;
        _userData = userData;
        _items = items;
    }

    public void setUserData(UserData userData){
        _userData = userData;
    }

    @Override
    public int getCount() {
        return _items != null? _items.size(): 0;
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
        SettingsItemEnum item = _items.get(position);
        LayoutInflater mInflater = (LayoutInflater)
                _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        switch (item){
            case Avatar:
                ImageLoader loader = new ImageLoader(_context);
                convertView = mInflater.inflate(R.layout.settings_avatar_item, null);
                ImageView imageView = (ImageView)convertView.findViewById(R.id.avatar_img);
                loader.displayImage(_userData.getUserImageUrl(), imageView);
                break;
            case Email:
                return getNonAvatarItemView(_context.getString(R.string.email), _userData.getEmail(), mInflater);
            case Name:
                return getNonAvatarItemView(_context.getString(R.string.name), _userData.getRealName(), mInflater);
            case Password:
                return getNonAvatarItemView("", _context.getString(R.string.change_password), mInflater);
        }
        return convertView;
    }

    private View getNonAvatarItemView(String header, String content, LayoutInflater mInflater){
        View view = mInflater.inflate(R.layout.settins_string_value_item, null);
        TextView textLbl = (TextView)view.findViewById(R.id.header_lbl);
        textLbl.setText(header);
        textLbl = (TextView)view.findViewById(R.id.content_lbl);
        textLbl.setText(content);
        return view;
    }
}
