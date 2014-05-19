package com.fantasysport.adapters.nonfantasy;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.fantasysport.R;
import com.fantasysport.models.NFGame;
import com.fantasysport.utility.image.ImageLoader;

import java.util.List;

/**
 * Created by bylynka on 5/16/14.
 */
public class NFCandidateGamesAdapter extends BaseAdapter {

    private List<NFGame> _games;
    private Context _context;
    private ImageLoader _imageLoader;
    private IListener _listener;

    public NFCandidateGamesAdapter(Context context, List<NFGame> games){
        _context = context;
        _games = games;
        _imageLoader = new ImageLoader(_context);
    }

    public void setListener(IListener listener){
        _listener = listener;
    }

    @Override
    public int getCount() {
        return _games == null? 0: _games.size();
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
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.candidate_nf_game_item, null);
        }
        final NFGame game = _games.get(position);

        ImageView homeTeamImg = (ImageView)convertView.findViewById(R.id.home_team_img);
        _imageLoader.displayImage(game.getHomeTeamLogo(), homeTeamImg);

        ImageView awayTeamImg = (ImageView)convertView.findViewById(R.id.away_team_img);
        _imageLoader.displayImage(game.getAwayTeamLogo(), awayTeamImg);

        TextView homeTeamTxt = (TextView)convertView.findViewById(R.id.home_team_lbl);
        homeTeamTxt.setText(game.getHomeTeamName());

        TextView awayTeamTxt = (TextView)convertView.findViewById(R.id.away_team_lbl);
        awayTeamTxt.setText(game.getAwayTeamName());

        TextView gameTimeTxt = (TextView)convertView.findViewById(R.id.game_time_lbl);
        gameTimeTxt.setText(game.getGameTime());

        homeTeamImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                raiseOnSelected(new NFGameWrapper(game, NFGameWrapper.SelectedTeamType.Home));
            }
        });

        awayTeamImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                raiseOnSelected(new NFGameWrapper(game, NFGameWrapper.SelectedTeamType.Away));
            }
        });

        return convertView;
    }

    private void raiseOnSelected(NFGameWrapper game){
        if(_listener == null){
            return;
        }
        _listener.onSelectedGame(game);
    }

    public interface IListener{

        public void onSelectedGame(NFGameWrapper game);
    }
}
