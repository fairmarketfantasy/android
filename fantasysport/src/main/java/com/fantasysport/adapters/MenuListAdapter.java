package com.fantasysport.adapters;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.fantasysport.R;
import com.fantasysport.models.UserData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 2/18/14.
 */
public class MenuListAdapter extends BaseExpandableListAdapter
        implements ExpandableListView.OnGroupCollapseListener, ExpandableListView.OnGroupExpandListener,
        ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener{

    private List<MenuItem> _items;
    private Context _context;
    private ExpandableListView _listView;
    private IOnItemClickListener _onItemClickListener;


    public MenuListAdapter(Context context, ExpandableListView listView, UserData data) {
        _context = context;
        _listView = listView;
        setMenu(data);

    }

    public void setMenu(UserData data){
        _items = new ArrayList<MenuItem>();
        MenuItem item = new MenuItem(MenuItemEnum.Sports, _context.getString(R.string.fantasy_sports));
        List<MenuItem> items = new ArrayList<MenuItem>();
        for (String sport : data.getActiveSports()){
            if(!data.getCurrentSport().equalsIgnoreCase(sport)){
                items.add(new MenuItem(MenuItemEnum.FantasySport, sport));
            }
        }
        item.setChildren(items);
        _items.add(item);
        _items.add(new MenuItem(MenuItemEnum.Predictions, _context.getString(R.string.predictions)));
        _items.add(new MenuItem(MenuItemEnum.Rules, _context.getString(R.string.rules)));
        _items.add(new MenuItem(MenuItemEnum.LegalStuff, _context.getString(R.string.legal_stuff)));
//        _items.add(new MenuItem(MenuItemEnum.Support, _context.getString(R.string.support)));
        _items.add(new MenuItem(MenuItemEnum.Settings, _context.getString(R.string.settings)));
        _items.add(new MenuItem(MenuItemEnum.SignOut, _context.getString(R.string.sign_out)));
        _listView.setOnGroupCollapseListener(this);
        _listView.setOnGroupExpandListener(this);
        _listView.setOnGroupClickListener(this);
        _listView.setOnChildClickListener(this);
    }

    public void setOnItemClickListener(IOnItemClickListener listener){
        _onItemClickListener = listener;
    }

    private void raiseOnItemClick(MenuItem item){
        if(_onItemClickListener == null){
            return;
        }
        _onItemClickListener.onClick(item);
    }

    @Override
    public int getGroupCount() {
        return _items.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        MenuItem item = _items.get(groupPosition);
        return item.hasChildren() ? item.getChildren().size() : 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return _items.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return _items.get(groupPosition).getChildren().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        MenuItem item = _items.get(groupPosition);
        if (item.hasChildren()) {
            return inflateParentItemView(item, convertView);
        } else {
            return inflateItemView(item, convertView);
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        MenuItem item = _items.get(groupPosition).getChildren().get(childPosition);
        return inflateItemView(item, convertView);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private View inflateParentItemView(MenuItem item, View convertView) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.group_menu_item, null);
        }
        TextView itemTxt = (TextView) convertView.findViewById(R.id.item);
        itemTxt.setText(item.getTitle());
//        setGroupIndicatorImg(convertView, false);
        return convertView;
    }

    private View inflateItemView(MenuItem item, View convertView) {
        LayoutInflater mInflater = (LayoutInflater)
                _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        int layoutId;
        if (item.getId() == MenuItemEnum.MLB) {
            layoutId = R.layout.sub_menu_item;
        }else {
            layoutId = R.layout.menu_item;
        }
        convertView = mInflater.inflate(layoutId, null);
        TextView itemTxt = (TextView) convertView.findViewById(R.id.item);
        itemTxt.setText(item.getTitle());

        return convertView;
    }



    private void setGroupIndicator(int groupPosition,  boolean isExpanded){
        MenuItem item = _items.get(groupPosition);
        if(!item.hasChildren()){
            return;
        }
        View view = _listView.getChildAt(groupPosition + 1);
        setGroupIndicatorImg(view, isExpanded);
    }

    private void setGroupIndicatorImg(View itemView, boolean isExpanded){
        ImageView img = (ImageView)itemView.findViewById(R.id.group_img);
        if(img == null){
            return;
        }
        Matrix matrix=new Matrix();
        Bitmap myImg = BitmapFactory.decodeResource(_context.getResources(), R.drawable.group_indicator_down);
        matrix.postRotate(isExpanded?180:0);
        Bitmap rotated = Bitmap.createBitmap(myImg, 0, 0, myImg.getWidth(), myImg.getHeight(), matrix, true);
//        myImg.recycle();
        img.setImageBitmap(rotated);
    }

    @Override
    public void onGroupCollapse(int groupPosition) {
        setGroupIndicator(groupPosition, false);
    }

    @Override
    public void onGroupExpand(int groupPosition) {
        setGroupIndicator(groupPosition, true);
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        MenuItem item = _items.get(groupPosition);
        if(item.hasChildren()){
            return false;
        }
        raiseOnItemClick(item);
        return true;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        MenuItem item = _items.get(groupPosition);
        raiseOnItemClick(item.getChildren().get(childPosition));
        return true;
    }

    public interface IOnItemClickListener{
        public void onClick(MenuItem item);
    }

}
