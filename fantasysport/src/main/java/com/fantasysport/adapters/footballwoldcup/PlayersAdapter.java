package com.fantasysport.adapters.footballwoldcup;

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
import com.fantasysport.models.fwc.Player;
import com.fantasysport.models.fwc.Team;
import com.fantasysport.utility.image.ImageLoader;

import java.util.List;

/**
 * Created by bylynka on 6/4/14.
 */
public class PlayersAdapter extends BaseAdapter {

    private Context _context;
    private List<Player> _players;
    private ImageLoader _imageLoader;
    private IListener _listener;

    public PlayersAdapter(Context context){
        _context = context;
        _imageLoader = new ImageLoader(_context);
        _imageLoader.setRequiredSize(_context.getResources().getDimensionPixelSize(R.dimen.flag_width), _context.getResources().getDimensionPixelSize(R.dimen.flag_height));
    }

    public void setListener(IListener listener){
        _listener = listener;
    }

    public void setPlayers(List<Player> players){
        _players = players;
    }

    public List<Player> getPlayers(){
        return _players;
    }

    @Override
    public int getCount() {
        return _players == null? 0: _players.size();
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
        if(convertView == null){
            LayoutInflater mInflater = (LayoutInflater)
                    _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.fwc_team_item, null);
        }
        Player player = _players.get(position);
        Button ptBtn = (Button)convertView.findViewById(R.id.pt_btn);
        ptBtn.setText(String.format("PT%.0f", player.getPT()));
        ptBtn.setEnabled(!player.isPredicted());
        ptBtn.setOnClickListener(new OnPlayerPtClickListener(player));
        TextView teamLbl = (TextView)convertView.findViewById(R.id.team_lbl);
        teamLbl.setText(player.getName());
        ImageView flagImg = (ImageView)convertView.findViewById(R.id.flag_img);
//        flagImg.setBackgroundDrawable(_context.getResources().getDrawable(R.drawable.flag));
        _imageLoader.displayImage(player.getLogoUrl(), flagImg);
        return convertView;
    }

    class OnPlayerPtClickListener implements View.OnClickListener {

        private Player _player;

        public OnPlayerPtClickListener(Player player){
            _player = player;
        }

        @Override
        public void onClick(View v) {
            _listener.onSubmittingTeam(_player);
        }
    }

    public interface IListener{
        void onSubmittingTeam(Player player);
    }
}