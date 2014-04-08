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
import com.fantasysport.activities.BaseActivity;
import com.fantasysport.fragments.PredictionRoster;
import com.fantasysport.models.IPlayer;
import com.fantasysport.models.Player;
import com.fantasysport.models.Roster;
import com.fantasysport.utility.image.ImageLoader;
import com.fantasysport.views.drawable.ColorViewDrawable;
import com.fantasysport.views.PlayerProgressTextView;

import java.util.List;

/**
 * Created by bylynka on 2/25/14.
 */
public class RosterPlayersAdapter extends BaseAdapter {

    private List<IPlayer> _players;
    private BaseActivity _context;
    private ImageLoader _imageLoader;
    private IListener _listener;
    private PredictionRoster _predictionRoster;
    private String _rosterState;

    public RosterPlayersAdapter(List<IPlayer> players, BaseActivity context, PredictionRoster predictionRoster) {
        _players = players;
        _context = context;
        _predictionRoster = predictionRoster;
        _imageLoader = new ImageLoader(_context);
    }


    public void setRosterState(String rosterState){
        _rosterState = rosterState;
    }

    public void setListener(IListener listener) {
        _listener = listener;
    }

    public List<IPlayer> getItems() {
        return _players;
    }

    public void setItems(List<IPlayer> items) {
        _players = items;
    }

    private void raisePT25PlayerEvent(Player player) {
        if (_listener != null) {
            _listener.onPT25Player(player);
        }
    }
    @Override
    public int getCount() {
        return _players != null ? _players.size() : 0;
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
        Button pt25Btn = null;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.player_item, null);
            final ColorViewDrawable viewDrawable = new ColorViewDrawable(Color.parseColor("#F0F0F0"), Color.parseColor("#D6D6D6"));
            viewDrawable.setStateListener(new ColorViewDrawableListener(convertView));
            convertView.setBackgroundDrawable(viewDrawable);
            pt25Btn = (Button) convertView.findViewById(R.id.pt25_btn);
            pt25Btn.setTypeface(_context.getProhibitionRound());
            tradeBtn = (Button) convertView.findViewById(R.id.trade_btn);
            tradeBtn.setTypeface(_context.getProhibitionRound());
        }
        IPlayer item = _players.get(position);
        boolean isSelected = item instanceof Player;
        Player player = null;
        if (isSelected) {
            player = (Player) item;
        }
        TextView gamePositionTxt = (TextView) convertView.findViewById(R.id.position_lbl);
        gamePositionTxt.setTypeface(_context.getRobotoThin());
        tradeBtn = (Button) convertView.findViewById(R.id.trade_btn);
        pt25Btn = (Button) convertView.findViewById(R.id.pt25_btn);
        TextView nameLbl = (TextView) convertView.findViewById(R.id.name_lbl);
        TextView priceLbl = (TextView) convertView.findViewById(R.id.buy_price_lbl);
        PlayerProgressTextView progressLbl = (PlayerProgressTextView) convertView.findViewById(R.id.progress_lbl);
        ImageView benchedImg = (ImageView) convertView.findViewById(R.id.benched_img);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.player_img);
        imageView.setImageBitmap(null);
        if (isSelected) {
            gamePositionTxt.setText(String.format("%s %s PPG %.1f", player.getPosition(), player.getTeam(), player.getPPG()));
            tradeBtn.setVisibility(View.VISIBLE);
            _imageLoader.displayImage(player.getImageUrl(), imageView);
            nameLbl.setVisibility(View.VISIBLE);
            nameLbl.setText(player.getName());
            priceLbl.setVisibility(View.VISIBLE);
            priceLbl.setText(String.format("$%.1f", player.getPurchasePrice()));
            if(_rosterState != null && _rosterState.equalsIgnoreCase(Roster.SUBMITTED)){
                progressLbl.setVisibility(View.VISIBLE);
                progressLbl.setPlayer(player);
            }else {
                progressLbl.setVisibility(View.INVISIBLE);
            }
            if (_predictionRoster != PredictionRoster.History) {
                tradeBtn.setOnClickListener(new TradeBtnClickListener(player));
            } else {
                tradeBtn.setVisibility(View.GONE);
            }
            if (!player.getIsBenched()&&(_rosterState.equalsIgnoreCase(Roster.SUBMITTED) || _rosterState.equalsIgnoreCase(Roster.IN_PROGRESS))) {
                pt25Btn.setVisibility(View.VISIBLE);
                pt25Btn.setOnClickListener(new PT25BtnClickListener(player));
            } else {
                pt25Btn.setVisibility(View.GONE);
                pt25Btn.setOnClickListener(null);
            }
            benchedImg.setVisibility(player.getIsBenched() ? View.VISIBLE : View.INVISIBLE);
        } else {
            gamePositionTxt.setText(item.getPosition() + " Not Selected");
            tradeBtn.setVisibility(View.GONE);
            pt25Btn.setVisibility(View.GONE);
            nameLbl.setVisibility(View.INVISIBLE);
            priceLbl.setVisibility(View.INVISIBLE);
            progressLbl.setVisibility(View.INVISIBLE);
            benchedImg.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private void raiseOnTrade(Player player) {
        if (_listener == null) {
            return;
        }
        _listener.onTrade(player);
    }

    public int getPosition(Player player) {
        if(_players == null){
            return -1;
        }
        return _players.indexOf(player);
    }

    class ColorViewDrawableListener implements ColorViewDrawable.IStateListener {

        private View _view;

        public ColorViewDrawableListener(View view) {
            _view = view;
        }

        @Override
        public void stateChanged(int state) {
            Drawable drawable;
            if (state == android.R.attr.state_pressed) {
                drawable = _context.getResources().getDrawable(R.drawable.circle_2_hover);
            } else {
                drawable = _context.getResources().getDrawable(R.drawable.circle_2);

            }
            setImage(_view, drawable);
        }
    }

    ;

    private void setImage(View v, Drawable drawable) {
        ImageView img = (ImageView) v.findViewById(R.id.circle_img);
        img.setImageDrawable(drawable);
    }

    public interface IListener {
        public void onTrade(Player player);
        public void onPT25Player(Player player);
    }

    public class TradeBtnClickListener implements View.OnClickListener {

        private Player _player;

        public TradeBtnClickListener(Player player) {
            _player = player;
        }

        @Override
        public void onClick(View v) {
            raiseOnTrade(_player);
        }

    }

    class PT25BtnClickListener implements View.OnClickListener {

        private Player _player;

        public PT25BtnClickListener(Player player) {
            _player = player;
        }

        @Override
        public void onClick(View v) {
            raisePT25PlayerEvent(_player);
        }
    }

}
