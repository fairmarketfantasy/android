package com.fantasysport.adapters.fantasy;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.fantasysport.R;
import com.fantasysport.adapters.ILoadListener;
import com.fantasysport.models.IndividualPrediction;
import com.fantasysport.models.Prediction;
import com.fantasysport.models.StatsItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by bylynka on 3/20/14.
 */
public class IndividualPredictionAdapter extends BaseAdapter {

    private List<IndividualPrediction> _items;
    private Context _context;
    private ILoadListener _listener;

    public IndividualPredictionAdapter(Context context, List<IndividualPrediction> items){
        _items = items;
        _context = context;
    }

    public void setLoadListener(ILoadListener listener){
        _listener = listener;
    }

    public void setItems(List<IndividualPrediction> items) {
        _items = items;
    }

    @Override
    public int getCount() {
        return _items == null? 0: _items.size();
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
        IndividualPrediction prediction = _items.get(position);
        if(prediction.isEmpty()){
            raiseOnLoad();
            return convertView;
        }

        if (convertView == null || convertView.getId() != R.id.root) {
            convertView = getLayoutInflater().inflate(R.layout.individual_prediction_item, null);
        }

        TextView nameLbl = (TextView)convertView.findViewById(R.id.name_lbl);
        nameLbl.setText(prediction.getMarketName());
        TextView contextTypeLbl = (TextView)convertView.findViewById(R.id.player_name_lbl);
        contextTypeLbl.setText(prediction.getPlayerName());
        TextView dayLbl = (TextView)convertView.findViewById(R.id.day_lbl);
        SimpleDateFormat sdf = new SimpleDateFormat("EE MM d");
        Date gameDate = prediction.getGameData();
        dayLbl.setText(gameDate != null? sdf.format(prediction.getGameData()) : "N/A");
        TextView gameTimeLbl = (TextView)convertView.findViewById(R.id.game_time_lbl);
        sdf = new SimpleDateFormat("K:mm a");
        gameTimeLbl.setText(gameDate != null? sdf.format(prediction.getGameData()): "N/A");
        TextView ptLbl = (TextView)convertView.findViewById(R.id.pt_lbl);
        ptLbl.setText(String.format("%.1f", prediction.getPT()));
        TextView awardLbl = (TextView)convertView.findViewById(R.id.award_lbl);
        awardLbl.setText(String.format("%.1f", prediction.getAward()));
        TextView predictionLbl  = (TextView)convertView.findViewById(R.id.prediction_lbl);
        String predictionText = "";
        List<StatsItem> statsItems = prediction.getEventPredictions();
        if(statsItems != null && statsItems.size()>0){
            StatsItem item = statsItems.get(0);
            predictionText = String.format("%s: %.2f %s",
                    item.getMode().equalsIgnoreCase(StatsItem.LESS_MODE)?"Under":"Over",
                    item.getValue(), item.getName());
            predictionLbl.setText(predictionText);
            convertView.findViewById(R.id.pred_header_lbl).setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.pred_dot).setVisibility(View.VISIBLE);
        }else{
            convertView.findViewById(R.id.pred_header_lbl).setVisibility(View.INVISIBLE);
            convertView.findViewById(R.id.pred_dot).setVisibility(View.INVISIBLE);
        }

        TextView resultLbl = (TextView)convertView.findViewById(R.id.result_lbl);
        String resultText = prediction.getState().equalsIgnoreCase(IndividualPrediction.CANCELED)
                ?_context.getString(R.string.did_not_play)
                : String.format("%.2f", prediction.getGameResult());
        resultLbl.setText(resultText);
        return convertView;
    }
}
