package com.fantasysport.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.fantasysport.R;
import com.fantasysport.views.Drawable.ColorViewDrawable;

import java.util.List;

/**
 * Created by bylynka on 2/25/14.
 */
public class RosterPlayersAdapter extends BaseAdapter {

    private List<PlayerItem> _players;
    private Context _context;

    public RosterPlayersAdapter(List<PlayerItem> players, Context context){
        _players = players;
        _context = context;
    }

    public List<PlayerItem> getItems(){
        return _players;
    }

    public void setItems(List<PlayerItem> items){
        _players = items;
    }

    @Override
    public int getCount() {
        return _players != null? _players.size(): 0;
    }

    @Override
    public Object getItem(int position) {
        return _players.get(position);
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
            convertView = mInflater.inflate(R.layout.player_item, null);
            final ColorViewDrawable viewDrawable = new ColorViewDrawable(Color.parseColor("#F0F0F0"), Color.parseColor("#D6D6D6"));
            final View finalConvertView = convertView;
            viewDrawable.setStateListener(new ColorViewDrawable.IStateListener() {
                @Override
                public void stateChanged(int state) {
                                        Drawable drawable;
                    if(state == android.R.attr.state_pressed){
                        drawable = _context.getResources().getDrawable(R.drawable.circle_2_hover);
                    }else {
                        drawable = _context.getResources().getDrawable(R.drawable.circle_2);

                    }
                    setImage(finalConvertView, drawable);
                }
            });
            convertView.setBackgroundDrawable(viewDrawable);
        }
        PlayerItem item = _players.get(position);
        boolean isSelected = item.isSelected();
        TextView gamePositionTxt = (TextView)convertView.findViewById(R.id.position_lbl);
        String gamePosition = item.getPosition() + (isSelected?"":" Not Selected");
        gamePositionTxt.setText(gamePosition);
        Button tradeBtn = (Button)convertView.findViewById(R.id.trade_btn);
        tradeBtn.setVisibility(isSelected?View.VISIBLE:View.GONE);
        return convertView;
    }

    private void setImage(View v, Drawable drawable){
        ImageView img = (ImageView)v.findViewById(R.id.circle_img);
        img.setImageDrawable(drawable);
    }
}
