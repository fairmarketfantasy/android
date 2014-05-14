package com.fantasysport.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import com.fantasysport.R;

/**
 * Created by bylynka on 3/25/14.
 */
public class HomeHistoryPredictionFragment extends BaseHomeFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_history_prediction_main, container, false);
        init();
        return _rootView;
    }

    @Override
    protected void init() {
        //
        _moneyTxt = getViewById(R.id.money_lbl);
        getBaseFFragment().addPageChangedListener(this);
        _pager = getViewById(R.id.pager);
        getBaseFFragment().addRosterLoadedListener(this);
        //
        setRoster();
        setPager(321);
        Button backBtn = getViewById(R.id.back_btn);
        backBtn.setOnClickListener(_backClickListener);
    }

    View.OnClickListener _backClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().finish();
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
}
