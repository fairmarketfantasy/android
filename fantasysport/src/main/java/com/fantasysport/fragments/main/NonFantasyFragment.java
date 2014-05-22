package com.fantasysport.fragments.main;

import android.os.Bundle;

import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.adapters.nonfantasy.NonFantasyPagerAdapter;
import com.fantasysport.fragments.NFMediator;
import com.fantasysport.models.NFData;
import com.fantasysport.models.nonfantasy.NFTeam;
import com.fantasysport.utility.CacheProvider;
import com.fantasysport.utility.DateUtils;
import com.fantasysport.webaccess.requestListeners.GetNFGamesResponseListener;
import com.fantasysport.webaccess.requestListeners.RequestError;

/**
 * Created by bylynka on 5/15/14.
 */
public class NonFantasyFragment extends BaseFragment
        implements NFMediator.ITeamSelectedListener, NFMediator.IUpdateGamesRequestListener {

    private NonFantasyPagerAdapter _pagerAdapter;
    private NFMediator _mediator = new NFMediator();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _mediator.addTeamSelectedListener(this);
        _mediator.addUpdateGamesRequestListener(this);
    }

    @Override
    protected void setPager() {
        super.setPager();
        _pagerAdapter = new NonFantasyPagerAdapter(getActivity().getSupportFragmentManager());
        _pager.setAdapter(_pagerAdapter);
        raiseOnPageChanged(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        long nowTime = DateUtils.getCurrentDate().getTime();
        long gamesTime = getStorage().getNFDataContainer().getUpdatedAt();
        long deltaTime = nowTime - gamesTime;
        long deltaTimeInMin = deltaTime / 60000;
        boolean isTimeZoneChanged = CacheProvider.getBoolean(getActivity(), Const.TIME_ZONE_CHANGED);
        CacheProvider.putBoolean(getActivity(), Const.TIME_ZONE_CHANGED ,false);
        if (deltaTimeInMin > 35 || isTimeZoneChanged) {
            loadNonFantasyGames();
        }
    }

    @Override
    protected void initStartParams(Bundle savedInstanceState) {

    }

    public NFMediator getMediator(){
        return _mediator;
    }

    @Override
    public void onSelectedTeam(Object sender, NFTeam team) {
        _pager.setCurrentItem(0, true);
    }

    private void loadNonFantasyGames(){
        String sport = "MLB";//_storage.getUserData().getCurrentSport();
        showProgress();
        getWebProxy().getNFGames(sport, new GetNFGamesResponseListener() {
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
        });
    }

    @Override
    public void onUpdatesGameRequest(Object sender) {
        loadNonFantasyGames();
    }

    @Override
    public void updateMainData(){
        loadNonFantasyGames();
    }
}
