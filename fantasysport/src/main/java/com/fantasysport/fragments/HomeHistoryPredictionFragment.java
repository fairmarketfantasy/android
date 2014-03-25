package com.fantasysport.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.fantasysport.R;
import com.fantasysport.webaccess.WebProxy;

/**
 * Created by bylynka on 3/25/14.
 */
public class HomeHistoryPredictionFragment extends BaseHomeFragment {

    public HomeHistoryPredictionFragment(WebProxy proxy, MainFragmentMediator fragmentMediator) {
        super(proxy, fragmentMediator);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_history_prediction_main, container, false);
        init();
        return _rootView;
    }

    @Override
    protected void init() {
        //
        getMainActivity().addListener(this);
        _pager = getViewById(R.id.pager);
        _moneyTxt = getViewById(R.id.money_lbl);
        _moneyTxt.setTypeface(getProhibitionRound());
        _listWrapper = getViewById(R.id.list_wrapper);
        _scrollView = getViewById(R.id.scroll_view);
        getMainActivity().addRosterLoadedListener(this);
        setUserData();
        //
        setRoster();
        setPager(321);
        initHeaderView();
        Button backBtn = getViewById(R.id.back_btn);
        backBtn.setTypeface(getProhibitionRound());
        backBtn.setOnClickListener(_backClickListener);
    }

    View.OnClickListener _backClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().finish();
        }
    };

}
