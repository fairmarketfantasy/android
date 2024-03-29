package com.fantasysport.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.models.Market;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bylynka on 2/24/14.
 */
public class GameItemFragment extends Fragment {

    private Market _market;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _market = (Market)getArguments().getSerializable(Const.MARKET);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.game_item_view, container, false);
        TextView marketNameTxt = (TextView)view.findViewById(R.id.game_name_lbl);
        marketNameTxt.setText(_market.getName());
        TextView marketTimeTxt = (TextView)view.findViewById(R.id.game_time_lbl);
        Date startDate = _market.getStartedAt();
        SimpleDateFormat sdf = new SimpleDateFormat("EE M @ K:mm a");
        marketTimeTxt.setText(sdf.format(startDate));
        return view;
    }

    public Market getMarket(){
        return _market;
    }
}