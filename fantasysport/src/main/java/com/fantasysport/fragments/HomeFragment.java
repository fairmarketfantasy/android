package com.fantasysport.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import com.fantasysport.R;
import com.fantasysport.activities.MainActivity;
import com.fantasysport.adapters.PlayerItem;
import com.fantasysport.models.IPlayer;
import com.fantasysport.models.Market;
import com.fantasysport.models.Player;
import com.fantasysport.models.Roster;
import com.fantasysport.views.Switcher;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.requestListeners.SubmitRosterResponseListener;
import com.fantasysport.webaccess.requests.SubmitRosterRequest;
import com.fantasysport.webaccess.responses.SubmitRosterResponse;

import java.util.List;

/**
 * Created by bylynka on 3/14/14.
 */
public class HomeFragment extends BaseHomeFragment implements AdapterView.OnItemClickListener,
        Switcher.ISelectedListener, MainActivity.IOnMarketsListener {

    private Button _submit100fbBtn;
    private Button _submitHth27fb;

    public HomeFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_main, container, false);
        init();
        ((MainActivity) getMainActivity()).addOnMarketsListener(this);
        return _rootView;
    }

    @Override
    protected void init() {
        super.init();
        _submit100fbBtn = getViewById(R.id.submit_100fb_btn);
        _submit100fbBtn.setOnClickListener(_submitClickListenere);
        _submitHth27fb = getViewById(R.id.submit_hth_btn);
        _submitHth27fb.setOnClickListener(_submitClickListenere);
        enableSubmitButtons();
    }

    private void enableSubmitButtons(){
        if(_submit100fbBtn == null || _submitHth27fb == null){
            return;
        }
        boolean canSubmitRoster = canSubmitRoster();
        _submit100fbBtn.setEnabled(canSubmitRoster);
        _submitHth27fb.setEnabled(canSubmitRoster);
    }


    @Override
    protected void setEmptyRoster() {
        super.setEmptyRoster();
       enableSubmitButtons();
    }

    protected boolean canSubmitRoster() {
        if (_playerAdapter == null ||
                _playerAdapter.getItems() == null ||
                _playerAdapter.getItems().size() == 0) {
            return false;
        }
        int counter = 0;
        for (IPlayer player : _playerAdapter.getItems()){
            if(player instanceof Player){
                counter++;
            }
        }
        return counter > 0;
    }

    @Override
    protected void updatePlayersList() {
        super.updatePlayersList();
        enableSubmitButtons();
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
                public void onRequestSuccess(SubmitRosterResponse response) {
                    setRoster((Roster) null);
                    setEmptyRoster();
                    dismissProgress();
                    String msg = response.getMessage();
                    if(msg != null){
                        showAlert("", msg);
                    }
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
        } else {
            setEmptyRoster();
        }
    }
}