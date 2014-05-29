package com.fantasysport.fragments.main;

import android.content.Intent;
import android.os.Bundle;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.adapters.nonfantasy.NonFantasyPagerAdapter;
import com.fantasysport.fragments.pages.nonfantasy.NFMediator;
import com.fantasysport.models.NFData;
import com.fantasysport.models.nonfantasy.NFAutoFillData;
import com.fantasysport.models.nonfantasy.NFTeam;
import com.fantasysport.webaccess.requestListeners.GetNFGamesResponseListener;
import com.fantasysport.webaccess.requestListeners.NFAutoFillResponseListener;
import com.fantasysport.webaccess.requestListeners.RequestError;

/**
 * Created by bylynka on 5/28/14.
 */
public class NFPredictionFragment extends BaseFragment
        implements NFMediator.ITeamSelectedListener, NFMediator.IUpdateGamesRequestListener,
        NFMediator.IAutoFillRequestListener, INFMainFragment {

    private NonFantasyPagerAdapter _pagerAdapter;
    private NFMediator _mediator = new NFMediator();
    private int _rosterId = -1;
    private NFData _data;
    private boolean _canEdit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _mediator.addTeamSelectedListener(this);
        _mediator.addUpdateGamesRequestListener(this);
        _mediator.addAutoFillRequestListener(this);
    }

    @Override
    protected void setPager() {
        super.setPager();
        _pagerAdapter = new NonFantasyPagerAdapter(getActivity().getSupportFragmentManager());
        _pager.setAdapter(_pagerAdapter);
        raiseOnPageChanged(0);
        loadNonFantasyGames();
    }

    @Override
    protected void initStartParams(Bundle savedInstanceState) {
        if(savedInstanceState == null){
            Intent intent = getActivity().getIntent();
            _rosterId = intent.getIntExtra(Const.ROSTER_ID, -1);
            _canEdit = intent.getBooleanExtra(Const.CAN_EDIT, false);
        }else {
            _rosterId = savedInstanceState.getInt(Const.ROSTER_ID, -1);
            _canEdit = savedInstanceState.getBoolean(Const.CAN_EDIT, false);
        }
    }

    public NFMediator getMediator(){
        return _mediator;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Const.ROSTER_ID, _rosterId);
        outState.putBoolean(Const.CAN_EDIT, true);
    }

    @Override
    public void onSelectedTeam(Object sender, NFTeam team) {
        _pager.setCurrentItem(0, true);
    }

    private void loadNonFantasyGames(){
        String sport = getStorage().getUserData().getSport();
        showProgress();
        getWebProxy().getNFRoster(sport, _rosterId, _getNFGamesResponseListener);

    }

    @Override
    public void onUpdatesGameRequest(Object sender) {
        loadNonFantasyGames();
    }

    @Override
    public void updateMainData(){
        loadNonFantasyGames();
    }

    @Override
    public void onRequestAutoFill(Object sender) {
        String sport = getStorage().getUserData().getSport();
        showProgress();
        getWebProxy().nfAutoFill(sport, new NFAutoFillResponseListener() {
            @Override
            public void onRequestError(RequestError error) {
                dismissProgress();
                showAlert(getString(R.string.error),  error.getMessage());
            }

            @Override
            public void onRequestSuccess(NFAutoFillData nfAutoFillData) {
                dismissProgress();
                _mediator.setNFAutoFillData(NFPredictionFragment.this, nfAutoFillData);
                _pager.setCurrentItem(0, true);
            }
        });
    }

    GetNFGamesResponseListener _getNFGamesResponseListener =  new GetNFGamesResponseListener() {
        @Override
        public void onRequestError(RequestError error) {
            dismissProgress();
            showAlert(getString(R.string.error), error.getMessage());
        }

        @Override
        public void onRequestSuccess(NFData response) {
            dismissProgress();
            _data = response;
            _mediator.gamesUpdated(this, response.getCandidateGames());
            _mediator.updateData(NFPredictionFragment.this);
        }
    };

    @Override
    public NFData getData() {
        return _data;
    }

    @Override
    public boolean isEditable() {
        return _canEdit;
    }
}