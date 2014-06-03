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
import com.fantasysport.models.fwc.Game;
import com.fantasysport.models.fwc.Team;
import com.fantasysport.models.nonfantasy.NFGame;
import com.fantasysport.models.nonfantasy.NFTeam;
import com.fantasysport.utility.image.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by bylynka on 6/3/14.
 */
public class GamesAdapter extends BaseAdapter {

    private List<Game> _games;
    private Context _context;
    private ImageLoader _imageLoader;
    private SimpleDateFormat _sdf = new SimpleDateFormat("d MMM K:mm a");

    public GamesAdapter(Context context){
        _context = context;
        _imageLoader = new ImageLoader(_context);
    }

    public void setGames(List<Game> games){
        _games = games;
    }

    @Override
    public int getCount() {
        return _games != null? _games.size():0;
    }

    @Override
    public Object getItem(int position) {
        return _games.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.fwc_game_item, null);
            holder = new Holder();
            holder.homeTeamImg = (ImageView) convertView.findViewById(R.id.home_team_img);
            holder.awayTeamImg = (ImageView) convertView.findViewById(R.id.away_team_img);
            holder.homeTeamTxt = (TextView) convertView.findViewById(R.id.home_team_lbl);
            holder.awayTeamTxt = (TextView) convertView.findViewById(R.id.away_team_lbl);
            holder.gameTimeTxt = (TextView) convertView.findViewById(R.id.game_time_lbl);
            holder.homePt = (Button) convertView.findViewById(R.id.home_team_pt_btn);
            holder.awayPt = (Button) convertView.findViewById(R.id.away_team_pt_btn);
            holder.addHomeImg = (ImageView) convertView.findViewById(R.id.add_home_img);
            holder.addAwayImg = (ImageView) convertView.findViewById(R.id.add_away_img);
            holder.separator = convertView.findViewById(R.id.separator_view);
            convertView.setTag(holder);
        }
        final Game game = _games.get(position);
        final Team homeTeam = game.getHomeTeam();
        final Team awayTeam = game.getAwayTeam();
        holder = (Holder) convertView.getTag();
        _imageLoader.displayImage(homeTeam.getLogoUrl(), holder.homeTeamImg);
        _imageLoader.displayImage(awayTeam.getLogoUrl(), holder.awayTeamImg);
        holder.homeTeamTxt.setText(homeTeam.getName());
        holder.awayTeamTxt.setText(awayTeam.getName());
        holder.gameTimeTxt.setText(_sdf.format(game.getGameDate()));
        holder.separator.setEnabled(!awayTeam.isPredicted() || !homeTeam.isPredicted());
        holder.homePt.setText(String.format("%.0f", homeTeam.getPT()));
        holder.awayPt.setText(String.format("%.0f", awayTeam.getPT()));
        return convertView;
    }

    public class Holder {

        public ImageView homeTeamImg;
        public ImageView awayTeamImg;
        public TextView homeTeamTxt;
        public TextView awayTeamTxt;
        public TextView gameTimeTxt;
        public Button homePt;
        public Button awayPt;
        public ImageView addHomeImg;
        public ImageView addAwayImg;
        public View separator;
    }
}
