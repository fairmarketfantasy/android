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
import com.fantasysport.models.nonfantasy.NFGame;
import com.fantasysport.models.nonfantasy.NFTeam;
import com.fantasysport.utility.image.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by bylynka on 5/16/14.
 */
public class NFCandidateGamesAdapter extends BaseAdapter {

    private List<NFGame> _games;
    private Context _context;
    private ImageLoader _imageLoader;
    private IListener _listener;
    private SimpleDateFormat _sdf = new SimpleDateFormat("d MMM K:mm a");

    public NFCandidateGamesAdapter(Context context, List<NFGame> games) {
        _context = context;
        _games = games;
        _imageLoader = new ImageLoader(_context);
    }

    public void setListener(IListener listener) {
        _listener = listener;
    }

    @Override
    public int getCount() {
        return _games == null ? 0 : _games.size();
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
            convertView = mInflater.inflate(R.layout.candidate_nf_game_item, null);
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
            holder.addHomeBtn = convertView.findViewById(R.id.add_home_btn);
            holder.addAwayBtn = convertView.findViewById(R.id.add_away_btn);
            holder.separator = convertView.findViewById(R.id.separator_view);
            convertView.setTag(holder);
        }
        final NFGame game = _games.get(position);
        final NFTeam homeTeam = game.getHomeTeam();
        final NFTeam awayTeam = game.getAwayTeam();
        holder = (Holder) convertView.getTag();
        _imageLoader.displayImage(homeTeam.getLogoUrl(), holder.homeTeamImg);
        _imageLoader.displayImage(awayTeam.getLogoUrl(), holder.awayTeamImg);
        holder.homeTeamTxt.setText(homeTeam.getName());
        holder.awayTeamTxt.setText(awayTeam.getName());
        holder.gameTimeTxt.setText(_sdf.format(game.getDate()));
        setPredictionButton(holder.homePt, homeTeam);
        setPredictionButton(holder.awayPt, awayTeam);
        holder.separator.setEnabled(!awayTeam.isPredicted() ||  !homeTeam.isPredicted());

        if (homeTeam.isSelected() && !awayTeam.isSelected()) {
            setPluses(holder, R.drawable.plus_disable, R.drawable.plus, null, new SelectTeamListener(awayTeam));
        } else if (!homeTeam.isSelected() && awayTeam.isSelected()) {
            setPluses(holder, R.drawable.plus, R.drawable.plus_disable, new SelectTeamListener(homeTeam),null);
        } else if (!homeTeam.isSelected() && !awayTeam.isSelected()) {
            setPluses(holder, R.drawable.plus, R.drawable.plus, new SelectTeamListener(homeTeam), new SelectTeamListener(awayTeam));
        }else {
            setPluses(holder, R.drawable.plus_disable, R.drawable.plus_disable, null, null);
        }
        return convertView;
    }

    private void setPredictionButton(Button btn, NFTeam team){
        btn.setEnabled(!team.isPredicted());
        btn.setText(String.format("PT%.0f", team.getPT()));
        if(team.isPredicted()){
            return;
        }
        btn.setOnClickListener(new PredictTeamListener(team));
    }

    private void setPluses(Holder holder,int homeResId, int awayResId, SelectTeamListener addHomeListener, SelectTeamListener addAwayListener){
        holder.addHomeImg.setImageResource(homeResId);
        holder.addAwayImg.setImageResource(awayResId);
        holder.addHomeBtn.setOnClickListener(addHomeListener);
        holder.addAwayBtn.setOnClickListener(addAwayListener);
    }

    private void raiseOnSelectedTeam(NFTeam team) {
        if (_listener == null) {
            return;
        }
        _listener.onSelectedTeam(team);
    }

    private void raiseOnPredictedTeam(NFTeam team) {
        if (_listener == null) {
            return;
        }
        _listener.onPT(team);
    }

    public List<NFGame> getGames() {
        return _games;
    }

    public void setGames(List<NFGame> games) {
        this._games = games;
    }

    public interface IListener {
        public void onSelectedTeam(NFTeam team);
        public void onPT(NFTeam team);
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
        public View addHomeBtn;
        public View addAwayBtn;
        public View separator;
    }

    class SelectTeamListener implements View.OnClickListener {

        private NFTeam _team;

        public SelectTeamListener(NFTeam team) {
            _team = team;
        }

        @Override
        public void onClick(View v) {
            _team.setIsSelected(true);
            raiseOnSelectedTeam(_team);
        }
    }

    class PredictTeamListener implements View.OnClickListener{

        private NFTeam _team;

        public PredictTeamListener(NFTeam team) {
            _team = team;
        }

        @Override
        public void onClick(View v) {
            raiseOnPredictedTeam(_team);
        }
    }
}
