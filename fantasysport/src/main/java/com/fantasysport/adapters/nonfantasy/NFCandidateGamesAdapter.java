package com.fantasysport.adapters.nonfantasy;

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
import com.fantasysport.models.NFGame;
import com.fantasysport.utility.Converter;
import com.fantasysport.utility.DateUtils;
import com.fantasysport.utility.DeviceInfo;
import com.fantasysport.utility.image.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        ViewContainer container;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.candidate_nf_game_item, null);
            container = new ViewContainer();
            container.homeTeamImg = (ImageView)convertView.findViewById(R.id.home_team_img);
            container.awayTeamImg = (ImageView)convertView.findViewById(R.id.away_team_img);
            container.homeTeamTxt = (TextView)convertView.findViewById(R.id.home_team_lbl);
            container.awayTeamTxt = (TextView)convertView.findViewById(R.id.away_team_lbl);
            container.gameTimeTxt = (TextView)convertView.findViewById(R.id.game_time_lbl);
            container.homePt = (Button)convertView.findViewById(R.id.home_team_pt_btn);
            container.awayPt = (Button)convertView.findViewById(R.id.away_team_pt_btn);
            convertView.setTag(container);
        }
        final NFGame game = _games.get(position);
        container = (ViewContainer)convertView.getTag();
        _imageLoader.displayImage(game.getHomeTeamLogo(), container.homeTeamImg);
        _imageLoader.displayImage(game.getAwayTeamLogo(), container.awayTeamImg);
        container.homeTeamTxt.setText(game.getHomeTeamName());
        container.awayTeamTxt.setText(game.getAwayTeamName());
        container.gameTimeTxt.setText(game.getFormattedGameTime());
        container.homePt.setText(String.format("PT%d", game.getHomeTeamPt()));
        container.awayPt.setText(String.format("PT%d", game.getAwayTeamPt()));
        convertView.findViewById(R.id.add_home_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                raiseOnSelected(new NFGameWrapper(game, NFGameWrapper.SelectedTeamType.Home));
            }
        });

        convertView.findViewById(R.id.add_away_img).setOnClickListener(new View.OnClickListener() {
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

    public class ViewContainer{

        public ImageView homeTeamImg;
        public ImageView awayTeamImg;
        public TextView homeTeamTxt;
        public TextView awayTeamTxt;
        public TextView gameTimeTxt;
        public Button homePt;
        public Button awayPt;
    }
}
