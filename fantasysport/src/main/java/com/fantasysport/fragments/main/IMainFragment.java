package com.fantasysport.fragments.main;

/**
 * Created by bylynka on 5/13/14.
 */
public interface IMainFragment {
    public void updateUserData(final Object initiator);
    public void addPageChangedListener(IPageChangedListener listener);

    public interface IPageChangedListener {
        public void onPageChanged(int page);
    }
}
