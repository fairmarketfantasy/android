package com.fantasysport.adapters.fantasy;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import com.fantasysport.R;
import com.fantasysport.adapters.ILoadListener;
import com.fantasysport.models.Prediction;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by bylynka on 3/19/14.
 */
public class PredictionAdapter extends BaseAdapter {

    private List<Prediction> _items;
    private Context _context;
    private ILoadListener _listener;
    private IOnShowRosterListener _onShowRosterListener;

    public PredictionAdapter(Context context, List<Prediction> items){
        _items = items;
        _context = context;
    }

    public void setOnShowRosterListener(IOnShowRosterListener onShowRosterListener){
        _onShowRosterListener = onShowRosterListener;
    }

    private void raiseOnShowRoster(Prediction prediction){
        if(_onShowRosterListener == null){
            return;
        }
        _onShowRosterListener.onShow(prediction);
    }

    public void setLoadListener(ILoadListener listener){
        _listener = listener;
    }

    @Override
    public int getCount() {
        return _items == null? 0: _items.size();
    }

    public void setItems(List<Prediction> items){
        _items = items;
    }

    @Override
    public Object getItem(int position) {
        return _items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public LayoutInflater getLayoutInflater(){
        return (LayoutInflater)_context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    private void raiseOnLoad(){
        if(_listener == null){
            return;
        }
        _listener.onLoad();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Prediction prediction = _items.get(position);
        if(prediction.isEmpty()){
            convertView = getLayoutInflater().inflate(R.layout.loading_item, null);
            raiseOnLoad();
            return convertView;
        }

        if (convertView == null || convertView.getId() != R.id.root) {
            convertView = getLayoutInflater().inflate(R.layout.prediction_item, null);
        }
        Button btn = (Button)convertView.findViewById(R.id.roster_btn);
        btn.setOnClickListener(new OnRosterBtnClickListener(prediction));
        TextView nameLbl = (TextView)convertView.findViewById(R.id.name_lbl);
        nameLbl.setText(prediction.getMarket().getName());
        TextView contextTypeLbl = (TextView)convertView.findViewById(R.id.contest_type_lbl);
        contextTypeLbl.setText(prediction.getContestType());
        TextView dayLbl = (TextView)convertView.findViewById(R.id.day_lbl);
        SimpleDateFormat sdf = new SimpleDateFormat("EE MM d");
        dayLbl.setText(sdf.format(prediction.getStartedAt()));
        TextView gameTimeLbl = (TextView)convertView.findViewById(R.id.game_time_lbl);
        sdf = new SimpleDateFormat("K:mm a");
        gameTimeLbl.setText(sdf.format(prediction.getStartedAt()));
        TextView stateLbl = (TextView)convertView.findViewById(R.id.state_lbl);
        stateLbl.setText(getStateText(prediction.getState()));
        TextView pointsLbl = (TextView)convertView.findViewById(R.id.funbucks_lbl);
        String pointsText = prediction.getState().compareToIgnoreCase("submitted") == 0? "N/A": String.format("%.2f", prediction.getScore());
        pointsLbl.setText(pointsText);
        TextView rankLbl  = (TextView)convertView.findViewById(R.id.rank_lbl);
        String rankText;
        if(prediction.getState().compareToIgnoreCase("submitted") == 0){
            rankText = "Not started yet";
        }else if(prediction.getState().compareToIgnoreCase("cancelled") == 0){
            rankText = "Canceled";
        }else{
            rankText = String.format("%d of %d", prediction.getRank(), prediction.getMaxEntries());
        }
        rankLbl.setText(rankText);
        TextView awardLbl = (TextView)convertView.findViewById(R.id.award_lbl);
        awardLbl.setText(prediction.getState().compareToIgnoreCase("finished") == 0? String.format("%.2f", (double)(prediction.getAward()/100)):"N/A");
        return convertView;
    }


    public String getStateText(String stateCode){
      if(stateCode == null){
          return "N/A";
      }else if(stateCode.equalsIgnoreCase(Prediction.CANCELED)){
          return "Canceled";
      }else if(stateCode.equalsIgnoreCase(Prediction.FINISHED)){
          return "Finished";
      }else if(stateCode.equalsIgnoreCase(Prediction.SUBMITTED)){
          return "Submitted";
      }else {
          return stateCode;
      }

    }

    public class OnRosterBtnClickListener implements View.OnClickListener{

        private Prediction _prediction;

        public OnRosterBtnClickListener(Prediction prediction){
            _prediction = prediction;
        }

        @Override
        public void onClick(View v) {
            raiseOnShowRoster(_prediction);
        }
    }



    public interface IOnShowRosterListener{
        public void onShow(Prediction prediction);
    }
}
