package com.fantasysport.fragments.main;

import com.fantasysport.fragments.pages.IMediator;

/**
 * Created by bylynka on 5/13/14.
 */
public interface IMainFragment {
    void addPageChangedListener(IPageChangedListener listener);
    void updateMainData();
    IMediator getMediator();

    public interface IPageChangedListener {
        public void onPageChanged(int page);
    }

}
