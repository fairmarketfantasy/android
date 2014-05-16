package com.fantasysport.fragments.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fantasysport.R;
import com.fantasysport.adapters.MainActivityPagerAdapter;
import com.fantasysport.fragments.BaseActivityFragment;
import com.fantasysport.fragments.PredictionRoster;
import com.fantasysport.views.AnimatedViewPager;

/**
 * Created by bylynka on 5/15/14.
 */
public class NonFantasyFragment extends BaseActivityFragment implements IMainFragment {


    protected AnimatedViewPager _pager;;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_root_main, container, false);
        initStartParams(savedInstanceState);
        setPager();
        return _rootView;
    }

    protected void initStartParams(Bundle savedInstanceState) {
    }


    protected void setPager() {
        _pager = getViewById(R.id.root_pager);
        _pager.setOnPageChangeListener(new AnimatedViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                raiseOnPageChanged(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        raiseOnPageChanged(0);
    }

    @Override
    public void updateUserData(Object initiator) {

    }

    @Override
    public void addPageChangedListener(IPageChangedListener listener) {

    }
}
