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
import com.fantasysport.models.nonfantasy.EmptyNFTeam;
import com.fantasysport.models.nonfantasy.INFTeam;
import com.fantasysport.models.nonfantasy.NFGame;
import com.fantasysport.models.nonfantasy.NFTeam;
import com.fantasysport.utility.image.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by bylynka on 5/19/14.
 */
public class NFRosterAdapter extends BaseAdapter {

    private List<INFTeam> _teams;
    private Context _context;
    private IListener _listener;
    private ImageLoader _imageLoader;
    private List<NFGame> _games;

    SimpleDateFormat _sdf = new SimpleDateFormat("d MMM K:mm a");

    public NFRosterAdapter(Context context){
        _context = context;
        _imageLoader = new ImageLoader(_context);
    }

    public void setGames(List<NFGame> games){
        _games = games;
    }

    public void setTeams(List<INFTeam> teams){
        _teams = teams;
    }

    public List<INFTeam> getTeams(){
        return _teams;
    }

    public void setListener(IListener listener){
        _listener = listener;
    }

    @Override
    public int getCount() {
        return _teams == null? 0 : _teams.size();
    }

    @Override
    public Object getItem(int position) {
        return _teams.get(position);
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
        INFTeam team = _teams.get(position);
        if(team instanceof NFTeam){
            try {
                prepareGameView(convertView, (NFTeam)team);
            }catch (Exception e){
                _teams.set(position, new EmptyNFTeam());
                prepareEmptyGameView(convertView);
            }
        }else {
            prepareEmptyGameView(convertView);
        }
        return convertView;
    }

    private void prepareGameView(View convertView,final NFTeam team) throws Exception {
        TextView teamLbl = (TextView)convertView.findViewById(R.id.team_lbl);
        teamLbl.setText(team.getName());
        teamLbl.setVisibility(View.VISIBLE);
        NFGame game = getGame(team.getGameStatsId());
        TextView gameLbl = (TextView)convertView.findViewById(R.id.game_name_lbl);
        gameLbl.setText(String.format("%s @ %s", game.getHomeTeam().getName(), game.getAwayTeam().getName()));
        gameLbl.setVisibility(View.VISIBLE);

        TextView gameTimeLbl = (TextView)convertView.findViewById(R.id.middle_lbl);
        gameTimeLbl.setText(_sdf.format(game.getDate()));

        Button ptBtn = (Button)convertView.findViewById(R.id.pt_btn);
        ptBtn.setVisibility(View.VISIBLE);

        Button dismissBtn = (Button)convertView.findViewById(R.id.dismiss_game_btn);
        ImageView logo = (ImageView)convertView.findViewById(R.id.circle_img);
        _imageLoader.displayImage(team.getLogoUrl(), logo);
        dismissBtn.setVisibility(View.VISIBLE);
        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                team.setIsSelected(false);
                raiseOnDismiss(team);
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

    private NFGame getGame(int statsId) throws Exception {
        for (NFGame game : _games){
            if(game.getStatsId() == statsId){
                return game;
            }
        }
        throw new Exception("Game is not found!");
    }

    private void raiseOnDismiss(NFTeam team){
        if(_listener == null){
            return;
        }
        _listener.onDismiss(team);
    }

    public interface IListener{
        public void onDismiss(NFTeam team);
    }

}
