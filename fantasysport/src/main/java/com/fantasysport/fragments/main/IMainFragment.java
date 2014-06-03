package com.fantasysport.fragments.main;

import com.fantasysport.fragments.pages.IMediator;

/**
 * Created by bylynka on 5/13/14.
 */
public interface IMainFragment {
    void addPageChangedListener(IPageChangedListener listener);
    void addPageAmountChangedListener(IPageAmountChangedListener listener);
    void updateMainData();
    IMediator getMediator();

    public interface IPageChangedListener {
        void onPageChanged(int page);
    }

    public interface IPageAmountChangedListener{
        void onPageAmountChanged(int amount);
    }

}
