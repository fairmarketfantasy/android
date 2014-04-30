package com.fantasysport.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.fantasysport.R;
import com.fantasysport.models.Player;
import com.fantasysport.utility.image.ImageLoader;

import java.util.List;


/**
 * Created by bylynka on 2/27/14.
 */
public class CandidatePlayersAdapter extends BaseAdapter {

    private Context _context;
    private List<Player> _players;
    private IListener _listener;
    public ImageLoader _imageLoader;

    public CandidatePlayersAdapter(Context context) {
        _context = context;
        _imageLoader = new ImageLoader(_context);
    }

    public void setListener(IListener listener) {
        _listener = listener;
    }

    public void setItems(List<Player> players) {
        _players = players;
    }

    public List<Player> getPlayers() {
        return _players;
    }

    @Override
    public int getCount() {
        return _players == null ? 0 : _players.size();
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
            convertView = mInflater.inflate(R.layout.candidate_player_item, null);
        }

        Player player = _players.get(position);

        ImageView benchedImg = (ImageView) convertView.findViewById(R.id.benched_img);
        benchedImg.setVisibility(player.getIsBenched() ? View.VISIBLE : View.INVISIBLE);

        Button pt25Btn = (Button) convertView.findViewById(R.id.pt25_btn);
        if (player.getIsBenched() || player.getPPG() == 0) {
            pt25Btn.setVisibility(View.INVISIBLE);
        } else {
            pt25Btn.setVisibility(View.VISIBLE);
            pt25Btn.setOnClickListener(new PT25BtnClickListener(player));
        }

        Button addBtn = (Button) convertView.findViewById(R.id.add_btn);
        addBtn.setOnClickListener(new AddBtnClickListener(player));

        TextView namelbl = (TextView) convertView.findViewById(R.id.name_lbl);
        namelbl.setText(player.getName());
        TextView positionLbl = (TextView) convertView.findViewById(R.id.position_lbl);
        positionLbl.setText(String.format("%s PPG %d", player.getTeam(), Math.round(player.getPPG())));
        TextView buyPriceLbl = (TextView) convertView.findViewById(R.id.buy_price_lbl);
        buyPriceLbl.setText(String.format("$%.0f", player.getBuyPrice()));
        ImageView imageView = (ImageView) convertView.findViewById(R.id.player_img);
        imageView.setImageBitmap(null);
        _imageLoader.displayImage(player.getImageUrl(), imageView);
        return convertView;
    }

    private void raiseAddPlayerEvent(Player player) {
        if (_listener != null) {
            _listener.onAddPlayer(player);
        }
    }

    private void raisePT25PlayerEvent(Player player) {
        if (_listener != null) {
            _listener.onPT25Player(player);
        }
    }

    public interface IListener {
        public void onAddPlayer(Player player);

        public void onPT25Player(Player player);
    }


    class AddBtnClickListener implements View.OnClickListener {

        private Player _player;

        public AddBtnClickListener(Player player) {
            _player = player;
        }

        @Override
        public void onClick(View v) {
            raiseAddPlayerEvent(_player);
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
