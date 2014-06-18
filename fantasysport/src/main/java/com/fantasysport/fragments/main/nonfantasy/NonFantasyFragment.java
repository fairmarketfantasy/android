package com.fantasysport.fragments.main.nonfantasy;

import android.os.Bundle;

import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.adapters.nonfantasy.NFPagerAdapter;
import com.fantasysport.fragments.main.BaseFragment;
import com.fantasysport.fragments.pages.nonfantasy.NFMediator;
import com.fantasysport.models.nonfantasy.NFData;
import com.fantasysport.models.nonfantasy.NFAutoFillData;
import com.fantasysport.models.nonfantasy.NFTeam;
import com.fantasysport.utility.CacheProvider;
import com.fantasysport.utility.DateUtils;
import com.fantasysport.views.ConfirmDialog;
import com.fantasysport.webaccess.responseListeners.GetNFGamesResponseListener;
import com.fantasysport.webaccess.responseListeners.NFAutoFillResponseListener;
import com.fantasysport.webaccess.responseListeners.RequestError;
import com.fantasysport.webaccess.responseListeners.StringResponseListener;

/**
 * Created by bylynka on 5/15/14.
 */
public class NonFantasyFragment extends BaseFragment
        implements NFMediator.ITeamSelectedListener, NFMediator.IUpdateGamesRequestListener,
        NFMediator.IAutoFillRequestListener, INFMainFragment, NFMediator.ISubmittingIndividualPredictionListener,
        NFMediator.IGamesShowingListener{

    private NFPagerAdapter _pagerAdapter;
    private NFMediator _mediator = new NFMediator();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _mediator.addTeamSelectedListener(this);
        _mediator.addUpdateGamesRequestListener(this);
        _mediator.addAutoFillRequestListener(this);
        _mediator.addSubmittingIndividualPrediction(this);
        _mediator.addGamesShowingListener(this);
    }

    @Override
    protected void setPager() {
        super.setPager();
        _pagerAdapter = new NFPagerAdapter(getActivity().getSupportFragmentManager());
        _pager.setAdapter(_pagerAdapter);
        raiseOnPageChanged(0);
        setPageAmount(2);
    }

    @Override
    public void onResume() {
        super.onResume();
        NFData data = getStorage().getNFDataContainer();
        if(data == null){
            return;
        }
        long nowTime = DateUtils.getCurrentDate().getTime();
        long gamesTime = data.getUpdatedAt();
        long deltaTime = nowTime - gamesTime;
        long deltaTimeInMin = deltaTime / 60000;
        boolean isTimeZoneChanged = CacheProvider.getBoolean(getActivity(), Const.TIME_ZONE_CHANGED);
        CacheProvider.putBoolean(getActivity(), Const.TIME_ZONE_CHANGED, false);
        if (deltaTimeInMin > 35 || isTimeZoneChanged) {
            loadNonFantasyGames();
        }
    }

    @Override
    protected void initStartParams(Bundle savedInstanceState) {
    }

    @Override
    public NFMediator getMediator() {
        return _mediator;
    }

    @Override
    public void onSelectedTeam(Object sender, NFTeam team) {
        _pager.setCurrentItem(0, true);
    }

    private void loadNonFantasyGames() {
        String sport = getStorage().getUserData().getSport();
        showProgress();
        getWebProxy().getNFGames(sport, _getNFGamesResponseListener);
    }

    @Override
    public void onUpdatesGameRequest(Object sender) {
        loadNonFantasyGames();
    }

    @Override
    public void updateData() {
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
                showAlert(getString(R.string.error), error.getMessage());
            }

            @Override
            public void onRequestSuccess(NFAutoFillData nfAutoFillData) {
                dismissProgress();
                _mediator.setNFAutoFillData(NonFantasyFragment.this, nfAutoFillData);
                _pager.setCurrentItem(0, true);
            }
        });
    }

    GetNFGamesResponseListener _getNFGamesResponseListener = new GetNFGamesResponseListener() {
        @Override
        public void onRequestError(RequestError error) {
            dismissProgress();
            showAlert(getString(R.string.error), error.getMessage());
        }

        @Override
        public void onRequestSuccess(NFData response) {
            dismissProgress();
            getStorage().setNFData(response);
            _mediator.gamesUpdated(this, response.getCandidateGames());
        }
    };

    @Override
    public NFData getData() {
        return getStorage().getNFDataContainer();
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public boolean isPredicted() {
        return false;
    }

    @Override
    public void onSubmittingIndividualPrediction(Object sender, final NFTeam team) {
        ConfirmDialog dialog = new ConfirmDialog(getActivity());
        dialog.setTitle(String.format("PT%.0f", team.getPT()))
                .setContent(String.format("Predict %s?", team.getName()))
                .setOkAction(new Runnable() {
                    @Override
                    public void run() {
                        team.setIsPredicted(true);
                        submitIndividualPrediction(team);
                        _mediator.submittedIndividualPrediction(NonFantasyFragment.this, team);
                    }
                })
                .show();

    }

    private void submitIndividualPrediction(final NFTeam team) {
        team.setIsPredicted(true);
        showProgress();
        getWebProxy().doNFIndividualPrediction(team, new StringResponseListener() {
            @Override
            public void onRequestError(RequestError error) {
                dismissProgress();
                showAlert(getString(R.string.error), error.getMessage());
                team.setIsPredicted(false);
                _mediator.submittedIndividualPrediction(NonFantasyFragment.this, team);
            }

            @Override
            public void onRequestSuccess(String msg) {
                dismissProgress();
                showAlert("INFO", msg);
            }
        });
    }

    @Override
    public void onGamesShowing(Object sender) {
        if(_pager == null){
            return;
        }
        _pager.setCurrentItem(1, true);
    }
}