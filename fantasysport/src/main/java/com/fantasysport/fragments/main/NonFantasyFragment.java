package com.fantasysport.fragments.main;

import android.os.Bundle;

import com.fantasysport.adapters.nonfantasy.NonFantasyPagerAdapter;
import com.fantasysport.fragments.NFMediator;
import com.fantasysport.models.nonfantasy.NFTeam;

/**
 * Created by bylynka on 5/15/14.
 */
public class NonFantasyFragment extends BaseFragment
        implements NFMediator.ITeamSelectedListener {

    private NonFantasyPagerAdapter _pagerAdapter;
    private NFMediator _mediator = new NFMediator();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _mediator.addTeamSelectedListener(this);
    }

    @Override
    protected void setPager() {
        super.setPager();
        _pagerAdapter = new NonFantasyPagerAdapter(getActivity().getSupportFragmentManager());
        _pager.setAdapter(_pagerAdapter);
        raiseOnPageChanged(0);
    }

    @Override
    protected void initStartParams(Bundle savedInstanceState) {

    }

    @Override
    public void updateUserData(Object initiator) {

    }

    public NFMediator getMediator(){
        return _mediator;
    }

    @Override
    public void onSelectedTeam(Object sender, NFTeam team) {
        _pager.setCurrentItem(0, true);
    }


}
