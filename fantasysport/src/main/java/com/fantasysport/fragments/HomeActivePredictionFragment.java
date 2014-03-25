package com.fantasysport.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import com.fantasysport.R;
import com.fantasysport.views.Switcher;
import com.fantasysport.webaccess.WebProxy;

/**
 * Created by bylynka on 3/25/14.
 */
public class HomeActivePredictionFragment extends BaseHomeFragment  implements AdapterView.OnItemClickListener, Switcher.ISelectedListener {

    public HomeActivePredictionFragment(WebProxy proxy, MainFragmentMediator fragmentMediator) {
        super(proxy, fragmentMediator);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_prediction_main, container, false);
        init();
        return _rootView;
    }

    @Override
    protected void init() {
        super.init();
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