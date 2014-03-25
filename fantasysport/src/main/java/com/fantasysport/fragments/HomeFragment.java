package com.fantasysport.fragments;


import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.fantasysport.R;
import com.fantasysport.adapters.GameAdapter;
import com.fantasysport.adapters.PlayerItem;
import com.fantasysport.adapters.RosterPlayersAdapter;
import com.fantasysport.models.*;
import com.fantasysport.views.Switcher;
import com.fantasysport.views.drawable.BitmapButtonDrawable;
import com.fantasysport.views.listeners.ViewPagerOnPageSelectedListener;
import com.fantasysport.webaccess.WebProxy;
import com.fantasysport.webaccess.requestListeners.AutofillResponseListener;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.requestListeners.SubmitRosterResponseListener;
import com.fantasysport.webaccess.requestListeners.TradePlayerResponseListener;
import com.fantasysport.webaccess.requests.SubmitRosterRequest;
import com.fantasysport.webaccess.responses.AutofillResponse;
import com.fantasysport.webaccess.responses.TradePlayerResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 3/14/14.
 */
public class HomeFragment extends BaseHomeFragment  implements AdapterView.OnItemClickListener, Switcher.ISelectedListener {

    public HomeFragment(WebProxy proxy, MainFragmentMediator fragmentMediator) {
        super(proxy, fragmentMediator);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_main, container, false);
        init();
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
            _webProxy.submitRoster(roster.getId(), contestType, new SubmitRosterResponseListener() {
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
}