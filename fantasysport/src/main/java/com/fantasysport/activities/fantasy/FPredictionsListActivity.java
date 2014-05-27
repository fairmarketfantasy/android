package com.fantasysport.activities.fantasy;

import android.support.v7.app.ActionBar;
import android.util.TypedValue;
import com.fantasysport.R;
import com.fantasysport.activities.PredictionsListActivity;
import com.fantasysport.adapters.PredictionsDropdownAdapter;

/**
 * Created by bylynka on 5/26/14.
 */
public class FPredictionsListActivity extends PredictionsListActivity {

    @Override
    protected void setActionBar(){
        super.setActionBar();
        setTopDropdown();
    }

    private void setTopDropdown(){
        getSupportActionBar().setCustomView(null);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        TypedValue tv = new TypedValue();
        int height = 0;
        if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true))
        {
            height = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        PredictionsDropdownAdapter adapter = new PredictionsDropdownAdapter(FPredictionsListActivity.this, height);
        adapter.setRosterHeader(getString(R.string.roster_predictions));
        adapter.setIndividualHeader(getString(R.string.individual_predictions));
        getSupportActionBar().setListNavigationCallbacks(adapter, this);
        getSupportActionBar().setSelectedNavigationItem(_currentPredictionType);
    }
}
