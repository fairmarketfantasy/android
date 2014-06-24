package com.fantasysport.fragments.pages.nonfantasy;

import android.os.Bundle;
import android.view.View;
import com.fantasysport.R;

/**
 * Created by bylynka on 6/24/14.
 */
public class PredictionGameRosterFragment extends BaseGameRosterFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _layoutId = R.layout.fragment_non_fantasy_prediction_roster_page;
    }

    @Override
    protected void init() {
        super.init();
        View autoFill = getViewById(R.id.autofill_holder);
        View bottomBar = getViewById(R.id.bottom_bar);
        if(getMainFragment().isEditable() && getMainFragment().canSubmit()){
            autoFill.setVisibility(View.VISIBLE);
            bottomBar.setVisibility(View.VISIBLE);
        }else {
            autoFill.setVisibility(View.GONE);
            bottomBar.setVisibility(View.GONE);
        }
    }
}
