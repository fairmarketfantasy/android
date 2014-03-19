package com.fantasysport.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.fantasysport.R;
import com.fantasysport.models.IPlayer;
import com.fantasysport.models.Player;
import com.fantasysport.utility.image.ImageLoader;
import com.fantasysport.views.drawable.ColorViewDrawable;
import com.fantasysport.views.PlayerProgressTextView;

import java.util.List;

/**
 * Created by bylynka on 2/25/14.
 */
public class RosterPlayersAdapter extends BaseAdapter {

    private List<IPlayer> _players;
    private Context _context;
    private Typeface _prohibitionRoundTypeFace;
    public ImageLoader _imageLoader;
    public IListener _listener;

    public RosterPlayersAdapter(List<IPlayer> players, Context context){
        _players = players;
        _context = context;
        _prohibitionRoundTypeFace  = Typeface.createFromAsset(_context.getAssets(), "fonts/ProhibitionRound.ttf");
        _imageLoader = new ImageLoader(_context);
    }

    public void setListener(IListener listener){
        _listener = listener;
    }

    public List<IPlayer> getItems(){
        return _players;
    }

    public void setItems(List<IPlayer> items){
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
        Button tradeBtn = null;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.player_item, null);
            final ColorViewDrawable viewDrawable = new ColorViewDrawable(Color.parseColor("#F0F0F0"), Color.parseColor("#D6D6D6"));
            viewDrawable.setStateListener(new ColorViewDrawableListener(convertView));
            convertView.setBackgroundDrawable(viewDrawable);
            tradeBtn = (Button)convertView.findViewById(R.id.trade_btn);
            tradeBtn.setTypeface(_prohibitionRoundTypeFace);
        }
        IPlayer item = _players.get(position);
        boolean isSelected = item instanceof Player;
        Player player = null;
        if(isSelected){
            player = (Player)item;
        }
        TextView gamePositionTxt = (TextView)convertView.findViewById(R.id.position_lbl);
        tradeBtn = (Button)convertView.findViewById(R.id.trade_btn);
        TextView nameLbl = (TextView)convertView.findViewById(R.id.name_lbl);
        TextView priceLbl = (TextView)convertView.findViewById(R.id.buy_price_lbl);
        PlayerProgressTextView progressLbl = (PlayerProgressTextView)convertView.findViewById(R.id.progress_lbl);
        ImageView benchedImg = (ImageView)convertView.findViewById(R.id.benched_img);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.player_img);
        imageView.setImageBitmap(null);
        if(isSelected){
            gamePositionTxt.setText(String.format("%s %s PPG %.1f", player.getPosition(), player.getTeam(), player.getPPG()));
            tradeBtn.setVisibility(View.VISIBLE);
            _imageLoader.displayImage(player.getImageUrl(), imageView);
            nameLbl.setVisibility(View.VISIBLE);
            nameLbl.setText(player.getName());
            priceLbl.setVisibility(View.VISIBLE);
            priceLbl.setText(String.format("$%.1f", player.getPurchasePrice()));
            progressLbl.setVisibility(View.VISIBLE);
            progressLbl.setPlayer(player);
            tradeBtn.setOnClickListener(new TradeBtnClickListener(player));
            benchedImg.setVisibility(player.getIsBenched()? View.VISIBLE : View.INVISIBLE);
        }else {
            gamePositionTxt.setText(item.getPosition() + " Not Selected");
            tradeBtn.setVisibility(View.GONE);
            nameLbl.setVisibility(View.INVISIBLE);
            priceLbl.setVisibility(View.INVISIBLE);
            progressLbl.setVisibility(View.INVISIBLE);
            benchedImg.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private void raiseOnTrade(Player player){
        if(_listener == null){
            return;
        }
        _listener.onTrade(player);
    }

    class ColorViewDrawableListener implements ColorViewDrawable.IStateListener {

        private View _view;

        public ColorViewDrawableListener(View view){
            _view = view;
        }

        @Override
        public void stateChanged(int state) {
            Drawable drawable;
            if(state == android.R.attr.state_pressed){
                drawable = _context.getResources().getDrawable(R.drawable.circle_2_hover);
            }else {
                drawable = _context.getResources().getDrawable(R.drawable.circle_2);

            }
            setImage(_view, drawable);
        }
    };

    private void setImage(View v, Drawable drawable){
        ImageView img = (ImageView)v.findViewById(R.id.circle_img);
        img.setImageDrawable(drawable);
    }

    public interface IListener{
        public void onTrade(Player player);
    }

    public class TradeBtnClickListener implements View.OnClickListener{

        private Player _player;

        public TradeBtnClickListener(Player player){
            _player = player;
        }

        @Override
        public void onClick(View v) {
            raiseOnTrade(_player);
        }

    }

}
