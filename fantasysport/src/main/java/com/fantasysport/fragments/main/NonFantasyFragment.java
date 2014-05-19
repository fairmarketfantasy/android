package com.fantasysport.fragments.main;

import android.os.Bundle;

import com.fantasysport.R;
import com.fantasysport.adapters.nonfantasy.NFGameWrapper;
import com.fantasysport.adapters.nonfantasy.NonFantasyPagerAdapter;
import com.fantasysport.fragments.NFMediator;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.requestListeners.SubmitNFRosterResponseListener;

import java.util.List;

/**
 * Created by bylynka on 5/15/14.
 */
public class NonFantasyFragment extends BaseFragment
        implements NFMediator.IGameSelectedListener {

    private NonFantasyPagerAdapter _pagerAdapter;
    private NFMediator _mediator = new NFMediator();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _mediator.addGameSelectedListener(this);
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
    public void onSelectedGame(Object sender, NFGameWrapper gameWrapper) {
        _pager.setCurrentItem(0, true);
    }


}
