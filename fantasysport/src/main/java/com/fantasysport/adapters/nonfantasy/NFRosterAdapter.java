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
import com.fantasysport.utility.image.ImageLoader;

import java.util.List;

/**
 * Created by bylynka on 5/19/14.
 */
public class NFRosterAdapter extends BaseAdapter {

    private List<INFGame> _games;
    private Context _context;
    private IListener _listener;
    private ImageLoader _imageLoader;

    public NFRosterAdapter(Context context){
        _context = context;
        _imageLoader = new ImageLoader(_context);
    }

    public void setGames(List<INFGame> games){
        _games = games;
    }

    public List<INFGame> getGames(){
        return _games;
    }

    public void setListener(IListener listener){
        _listener = listener;
    }

    @Override
    public int getCount() {
        return _games == null? 0 : _games.size();
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
        if(convertView == null){
            LayoutInflater mInflater = (LayoutInflater)
                    _context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.roster_nf_game_item, null);
        }
        INFGame game = _games.get(position);
        if(game instanceof NFGameWrapper){
            prepareGameView(convertView, (NFGameWrapper)game);
        }else {
            prepareEmptyGameView(convertView);
        }
        return convertView;
    }

    private void prepareGameView(View convertView,final NFGameWrapper gameWrapper){
        NFGame game = gameWrapper.getGame();
        TextView teamLbl = (TextView)convertView.findViewById(R.id.team_lbl);
        teamLbl.setText(gameWrapper.getSelectedTeamType() == NFGameWrapper.SelectedTeamType.Home? game.getHomeTeamName(): game.getAwayTeamName());
        teamLbl.setVisibility(View.VISIBLE);

        TextView gameLbl = (TextView)convertView.findViewById(R.id.game_name_lbl);
        gameLbl.setText(String.format("%s @ %s", game.getHomeTeamName(), game.getAwayTeamName()));
        gameLbl.setVisibility(View.VISIBLE);

        TextView gameTimeLbl = (TextView)convertView.findViewById(R.id.middle_lbl);
        gameTimeLbl.setText(game.getFormattedGameTime());

        Button ptBtn = (Button)convertView.findViewById(R.id.pt_btn);
        ptBtn.setVisibility(View.VISIBLE);

        Button dismissBtn = (Button)convertView.findViewById(R.id.dismiss_game_btn);
        ImageView logo = (ImageView)convertView.findViewById(R.id.circle_img);
        _imageLoader.displayImage(gameWrapper.getSelectedTeamType() == NFGameWrapper.SelectedTeamType.Home? game.getHomeTeamLogo(): game.getAwayTeamLogo(), logo);
        dismissBtn.setVisibility(View.VISIBLE);
        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                raiseOnDismiss(gameWrapper);
            }
        });
    }

    private void prepareEmptyGameView(View convertView){
        convertView.findViewById(R.id.team_lbl).setVisibility(View.INVISIBLE);
        convertView.findViewById(R.id.game_name_lbl).setVisibility(View.INVISIBLE);
        convertView.findViewById(R.id.pt_btn).setVisibility(View.INVISIBLE);
        convertView.findViewById(R.id.dismiss_game_btn).setVisibility(View.INVISIBLE);
        ImageView logo = (ImageView)convertView.findViewById(R.id.circle_img);
        logo.setImageResource(R.drawable.circle_2);
        TextView lbl = (TextView)convertView.findViewById(R.id.middle_lbl);
        lbl.setText("Team Not Selected");
    }

    private void raiseOnDismiss(NFGameWrapper gameWrapper){
        if(_listener == null){
            return;
        }
        _listener.onDismiss(gameWrapper);
    }

    public interface IListener{

        public void onDismiss(NFGameWrapper gameWrapper);

    }

}
