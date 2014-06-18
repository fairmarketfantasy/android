package com.fantasysport.adapters.fantasy;

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
import com.fantasysport.models.IndividualPrediction;

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
    private SimpleDateFormat _gameDateFormat = new SimpleDateFormat("EE MM d");
    private SimpleDateFormat _gameTimeFormat = new SimpleDateFormat("K:mm a");
    private ITradeListener _tradeListener;
    private boolean _canTradeSubmitted = false;

    public IndividualPredictionAdapter(Context context, List<IndividualPrediction> items){
        _items = items;
        _context = context;
    }

    public void setCanTradeSubmitted(boolean value){
        _canTradeSubmitted = value;
    }

    public void setTradeListener(ITradeListener listener){
        _tradeListener = listener;
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

        Holder holder;
        if (convertView == null || convertView.getId() != R.id.root) {
            convertView = getLayoutInflater().inflate(R.layout.individual_prediction_item, null);
            holder = new Holder();
            holder.nameLbl = (TextView)convertView.findViewById(R.id.name_lbl);
            holder.contextTypeLbl = (TextView)convertView.findViewById(R.id.player_name_lbl);
            holder.dayLbl = (TextView)convertView.findViewById(R.id.day_lbl);
            holder.gameTimeLbl = (TextView)convertView.findViewById(R.id.game_time_lbl);
            holder.ptLbl = (TextView)convertView.findViewById(R.id.pt_lbl);
            holder.awardLbl = (TextView)convertView.findViewById(R.id.award_lbl);
            holder.predictionLbl = (TextView)convertView.findViewById(R.id.prediction_lbl);
            holder.resultLbl = (TextView)convertView.findViewById(R.id.result_lbl);
            holder.tradeBtn = (Button)convertView.findViewById(R.id.trade_btn);
            convertView.setTag(holder);
        }
        holder = (Holder)convertView.getTag();
        holder.nameLbl.setText(prediction.getMarketName());
        holder.contextTypeLbl.setText(prediction.getPlayerName());
        Date gameDate = prediction.getGameData();
        holder.dayLbl.setText(gameDate != null? _gameDateFormat.format(prediction.getGameData()) : "N/A");
        holder.gameTimeLbl.setText(gameDate != null ? _gameTimeFormat.format(prediction.getGameData()) : "N/A");
        holder.ptLbl.setText(String.format("%.1f", prediction.getPT()));
        holder.awardLbl.setText(getAward(prediction));
        List<StatsItem> statsItems = prediction.getEventPredictions();
        if(statsItems != null && statsItems.size()>0){
            StatsItem item = statsItems.get(0);
            String predictionText = String.format("%s: %.2f %s",
                    item.getMode().equalsIgnoreCase(StatsItem.LESS_MODE)?"Under":"Over",
                    item.getValue(), item.getName());
            holder.predictionLbl.setText(predictionText);
            convertView.findViewById(R.id.pred_header_lbl).setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.pred_dot).setVisibility(View.VISIBLE);
        }else{
            convertView.findViewById(R.id.pred_header_lbl).setVisibility(View.INVISIBLE);
            convertView.findViewById(R.id.pred_dot).setVisibility(View.INVISIBLE);
        }
        holder.resultLbl.setText(getResult(prediction));
        setTradeBtn(holder.tradeBtn, prediction);
        return convertView;
    }

    private void setTradeBtn(Button btn, final IndividualPrediction prediction){
        if(prediction == null || btn == null){
            return;
        }
        if(prediction.getState().equalsIgnoreCase(IndividualPrediction.SUBMITTED) && _canTradeSubmitted && prediction.canTrade()){
            btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    raiseOnTrade(prediction);
                }
            });
        }else{
            btn.setVisibility(View.INVISIBLE);
        }
    }

    private String getAward(IndividualPrediction prediction){
        if(prediction.getState().equalsIgnoreCase(IndividualPrediction.CANCELED)){
            return _context.getString(R.string.did_not_play);
        }else if(prediction.getState().equalsIgnoreCase(IndividualPrediction.FINISHED)){
            return "N/A";
        }else{
            return String.format("%.1f", prediction.getAward());
        }
    }

    private String getResult(IndividualPrediction prediction){
        if(prediction.getState().equalsIgnoreCase(IndividualPrediction.CANCELED)){
            return _context.getString(R.string.did_not_play);
        }else if(prediction.getState().equalsIgnoreCase(IndividualPrediction.SUBMITTED)){
            return "N/A";
        }else{
            return prediction.getGameResult();
        }
    }

    private void raiseOnTrade(IndividualPrediction prediction){
        if(_tradeListener == null){
            return;
        }
        _tradeListener.onTrade(prediction);
    }

    public interface ITradeListener{
        void onTrade(IndividualPrediction prediction);
    }

    class Holder{
        TextView nameLbl;
        TextView contextTypeLbl;
        TextView dayLbl;
        TextView gameTimeLbl;
        TextView ptLbl;
        TextView awardLbl;
        TextView predictionLbl;
        TextView resultLbl;
        Button tradeBtn;
    }
}
