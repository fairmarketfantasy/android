package com.fantasysport.fragments.main;

/**
 * Created by bylynka on 5/13/14.
 */
public interface IMainFragment {
    public void addPageChangedListener(IPageChangedListener listener);
    public void updateMainData();

    public interface IPageChangedListener {
        public void onPageChanged(int page);
    }
}
