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
    protected List<IMainFragment.IPageChangedListener> _pageChangedListener = new ArrayList<IMainFragment.IPageChangedListener>();
    protected List<IMainFragment.IPageAmountChangedListener> _pageAmountChangedListeners = new ArrayList<IMainFragment.IPageAmountChangedListener>();
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

    @Override
    public void addPageChangedListener(IPageChangedListener listener) {
        _pageChangedListener.add(listener);
    }

    public void addPageAmountChangedListener(IPageAmountChangedListener listener){
        _pageAmountChangedListeners.add(listener);
    }

    protected void raiseOnPageChanged(int page) {
        for (int i = 0; i < _pageChangedListener.size(); i++) {
            _pageChangedListener.get(i).onPageChanged(page);
        }
    }

    protected void raiseOnPagesAmountChanged(int amount) {
        for (int i = 0; i < _pageAmountChangedListeners.size(); i++) {
            _pageAmountChangedListeners.get(i).onPageAmountChanged(amount);
        }
    }

    protected void setPageAmount(int amount){
        raiseOnPagesAmountChanged(amount);
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
