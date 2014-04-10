package com.fantasysport.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import com.fantasysport.R;
import com.fantasysport.activities.MainActivity;
import com.fantasysport.models.Market;
import com.fantasysport.models.Roster;
import com.fantasysport.views.Switcher;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.requestListeners.SubmitRosterResponseListener;
import com.fantasysport.webaccess.requests.SubmitRosterRequest;

import java.util.List;

/**
 * Created by bylynka on 3/14/14.
 */
public class HomeFragment extends BaseHomeFragment  implements AdapterView.OnItemClickListener,
        Switcher.ISelectedListener, MainActivity.IOnMarketsListener{


    public HomeFragment(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_main, container, false);
        init();
        ((MainActivity)getMainActivity()).addOnMarketsListener(this);
        return _rootView;
    }

    @Override
    protected void init() {
        super.init();
        Button submit100fbBtn = getViewById(R.id.submit_100fb_btn);
        submit100fbBtn.setTypeface(getProhibitionRound());
        submit100fbBtn.setOnClickListener(_submitClickListenere);
        Button submitHth27fb = getViewById(R.id.submit_hth_btn);
        submitHth27fb.setTypeface(getProhibitionRound());
        submitHth27fb.setOnClickListener(_submitClickListenere);
    }

    View.OnClickListener _submitClickListenere = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
       Roster roster = getRoster();
            if (roster == null) {
                showAlert("", getString(R.string.fill_roster));
                return;
            }
            showProgress();
            String contestType = v.getId() == R.id.submit_100fb_btn ? SubmitRosterRequest.TOP6 : SubmitRosterRequest.H2H;
            getWebProxy().submitRoster(roster.getId(), contestType, new SubmitRosterResponseListener() {
                @Override
                public void onRequestError(RequestError error) {
                    dismissProgress();
                    showAlert(getString(R.string.error), error.getMessage());
                }

                @Override
                public void onRequestSuccess(Object o) {
                    setRoster((Roster) null);
                    setEmptyRoster();
                    dismissProgress();
                }
            });
        }
    };

    @Override
    public void onMarkets(List<Market> markets) {
        updateMarkets();
        setRoster((Roster) null);
        if (markets != null && markets.size() > 0) {
            _pager.setCurrentItem(0);
            setNewRoster(markets.get(0));
        }else {
            setEmptyRoster();
        }
    }
}