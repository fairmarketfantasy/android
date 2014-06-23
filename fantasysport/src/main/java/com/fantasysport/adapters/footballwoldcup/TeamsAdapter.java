package com.fantasysport.adapters.footballwoldcup;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.fantasysport.R;
import com.fantasysport.models.fwc.Team;
import com.fantasysport.utility.image.ImageLoader;

import java.util.List;

/**
 * Created by bylynka on 6/3/14.
 */
public class TeamsAdapter extends BaseAdapter{

    private Context _context;
    private List<Team> _teams;
    private ImageLoader _imageLoader;
    private IListener _listener;
    private Bitmap _flag;

    public TeamsAdapter(Context context){
        _context = context;
        _imageLoader = new ImageLoader(_context);
        _flag = BitmapFactory.decodeResource(context.getResources(),R.drawable.flag);
    }

    public void setListener(IListener listener){
        _listener = listener;
    }

    public void setTeams(List<Team> teams){
        _teams = teams;
    }

    @Override
    public int getCount() {
        return _teams == null? 0: _teams.size();
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
            convertView = mInflater.inflate(R.layout.fwc_team_item, null);
        }
        Team team = _teams.get(position);
        Button ptBtn = (Button)convertView.findViewById(R.id.pt_btn);
        if(team.hasPT()){
            ptBtn.setText(String.format("PT%.0f", team.getPT()));
            ptBtn.setEnabled(!team.isPredicted());
            ptBtn.setOnClickListener(new OnTeamPtClickListener(team));
            ptBtn.setVisibility(View.VISIBLE);
        }else {
            ptBtn.setOnClickListener(null);
            ptBtn.setVisibility(View.INVISIBLE);
        }
        TextView teamLbl = (TextView)convertView.findViewById(R.id.team_lbl);
        teamLbl.setText(team.getName());
        ImageView flagImg = (ImageView)convertView.findViewById(R.id.flag_img);
        flagImg.setImageBitmap(_flag);
        _imageLoader.displayImage(team.getLogoUrl(), flagImg);
        return convertView;
    }

    public List<Team> getTeams() {
        return _teams;
    }

    class OnTeamPtClickListener implements View.OnClickListener {

        private Team _team;

        public OnTeamPtClickListener(Team team){
            _team = team;
        }

        @Override
        public void onClick(View v) {
            _listener.onSubmittingTeam(_team);
        }
    }

    public interface IListener{
        void onSubmittingTeam(Team team);
    }
}
