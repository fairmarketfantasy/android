package com.fantasysport.fragments.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fantasysport.R;
import com.fantasysport.fragments.BaseActivityFragment;
import com.fantasysport.views.AnimatedViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 5/16/14.
 */
public abstract class BaseFragment extends BaseActivityFragment implements IMainFragment  {

    protected AnimatedViewPager _pager;
    protected List<IPageChangedListener> _listeners = new ArrayList<IPageChangedListener>();
    protected IOnAttachedListener _onAttachedListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_root_main, container, false);
        initStartParams(savedInstanceState);
        setPager();
        return _rootView;
    }

    protected abstract void initStartParams(Bundle savedInstanceState);

    public void setOnAttachedListener(IOnAttachedListener listener){
        _onAttachedListener = listener;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(_onAttachedListener != null){
            _onAttachedListener.onAttached();
        }
    }

    protected void setPager(){
        _pager = getViewById(R.id.root_pager);
        _pager.setOnPageChangeListener(_pageChangeListener);
    }

    public void addPageChangedListener(IPageChangedListener listener) {
        _listeners.add(listener);
    }

    protected void raiseOnPageChanged(int page) {
        for (int i = 0; i < _listeners.size(); i++) {
            _listeners.get(i).onPageChanged(page);
        }
    }

    protected AnimatedViewPager.OnPageChangeListener _pageChangeListener =  new AnimatedViewPager.OnPageChangeListener() {
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
    };

    public interface IOnAttachedListener{
        void onAttached();
    }
}
