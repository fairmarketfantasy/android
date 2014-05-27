package com.fantasysport.adapters.nonfantasy;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.fantasysport.R;
import com.fantasysport.adapters.ILoadListener;
import com.fantasysport.models.Prediction;
import com.fantasysport.models.nonfantasy.NFPrediction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by bylynka on 5/27/14.
 */
public class NFPredictionAdapter extends BaseAdapter {

    private Context _context;
    private List<NFPrediction> _predictions;
    private ILoadListener _listener;
    private IOnShowRosterListener _onShowRosterListener;


    public NFPredictionAdapter(Context context) {
        _context = context;
    }

    public void setPredictions(List<NFPrediction> predictions) {
        _predictions = predictions;
    }

    public void setOnShowRosterListener(IOnShowRosterListener onShowRosterListener) {
        _onShowRosterListener = onShowRosterListener;
    }

    private void raiseOnShowRoster(NFPrediction prediction) {
        if (_onShowRosterListener == null) {
            return;
        }
        _onShowRosterListener.onShow(prediction);
    }

    public void setLoadListener(ILoadListener listener) {
        _listener = listener;
    }

    private void raiseOnLoad() {
        if (_listener == null) {
            return;
        }
        _listener.onLoad();
    }

    @Override
    public int getCount() {
        return _predictions == null ? 0 : _predictions.size();
    }

    @Override
    public Object getItem(int position) {
        return _predictions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;
        if (convertView == null) {
           convertView = getLayoutInflater().inflate(R.layout.prediction_item, null);
            convertView.findViewById(R.id.contest_type_lbl).setVisibility(View.INVISIBLE);
            holder = new Holder();
            holder.nameLbl = (TextView)convertView.findViewById(R.id.name_lbl);
            holder.dayLbl = (TextView)convertView.findViewById(R.id.day_lbl);
            holder.stateLbl = (TextView)convertView.findViewById(R.id.state_lbl);
            holder.pointsLbl = (TextView)convertView.findViewById(R.id.funbucks_lbl);
            holder.gameTimeLbl = (TextView)convertView.findViewById(R.id.game_time_lbl);
            holder.rankLbl = (TextView)convertView.findViewById(R.id.rank_lbl);
            holder.awardLbl = (TextView)convertView.findViewById(R.id.award_lbl);
            holder.root = convertView.findViewById(R.id.root);
            holder.rosterBtn = (Button)convertView.findViewById(R.id.roster_btn);
            convertView.setTag(holder);
        }
        holder = (Holder)convertView.getTag();
        NFPrediction prediction = _predictions.get(position);
        if(prediction.isEmpty()){
            holder.root.setVisibility(View.GONE);
            raiseOnLoad();
        }else {
            holder.root.setVisibility(View.VISIBLE);
            holder.nameLbl.setText("100/30/30");
            SimpleDateFormat sdf = new SimpleDateFormat("EE MM d");
            Date date = prediction.getDate();
            holder.dayLbl.setText(sdf.format(date));
            sdf = new SimpleDateFormat("K:mm a");
            holder.gameTimeLbl.setText(sdf.format(date));
            holder.stateLbl.setText(getState(prediction.getState()));
            holder.rankLbl.setText(getRank(prediction));
            holder.pointsLbl.setText(getPoints(prediction));
            holder.awardLbl.setText(getAward(prediction));
            holder.rosterBtn.setOnClickListener(new RosterBtnListener(prediction));

        }
        return convertView;
    }

    private String getAward(NFPrediction prediction){
        switch (prediction.getState()){
            case Finished:
                return "!!!";
            default:
                return "N/A";
        }
    }

    private String getPoints(NFPrediction prediction){
        switch (prediction.getState()){
            case Finished:
                return String.format("%.1f", prediction.getPoints());
            default:
                return "N/A";
        }
    }

    private String getRank(NFPrediction prediction){
        switch (prediction.getState()){
            case Finished:

                return prediction.getRank()== null?"N/A": Integer.toString(prediction.getRank());
            case Submitted:
                return "Not started yet";
            default:
                return "N/A";
        }
    }

    private String getState(NFPrediction.State state){
        switch (state){
            case Canceled:
                return "Canceled";
            case Finished:
                return "Finished";
            case Submitted:
                return "Submitted";
            default:
                return "N/A";
        }
    }

    public LayoutInflater getLayoutInflater(){
        return (LayoutInflater)_context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    public interface IOnShowRosterListener {
        public void onShow(NFPrediction prediction);
    }

    class Holder{
        TextView nameLbl;
        TextView dayLbl;
        TextView stateLbl;
        TextView pointsLbl;
        TextView gameTimeLbl;
        TextView rankLbl;
        TextView awardLbl;
        View root;
        Button rosterBtn;
    }

    class RosterBtnListener implements View.OnClickListener{

       private NFPrediction _prediction;

        public RosterBtnListener(NFPrediction prediction){
            _prediction = prediction;
        }

        @Override
        public void onClick(View v) {
            raiseOnShowRoster(_prediction);
        }
    }
}
