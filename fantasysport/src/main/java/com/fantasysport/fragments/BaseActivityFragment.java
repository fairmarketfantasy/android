package com.fantasysport.fragments;


import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.view.View;
import com.fantasysport.activities.BaseActivity;
import com.fantasysport.repo.Storage;
import com.fantasysport.webaccess.WebProxy;

/**
 * Created by bylynka on 3/14/14.
 */
public class BaseActivityFragment extends Fragment {

    protected Storage _storage;
    protected WebProxy _webProxy;
    protected View _rootView;

    public BaseActivityFragment(WebProxy proxy){
        _storage = Storage.instance();
        _webProxy = proxy;
    }


    protected <T> T getViewById(int viewId){
        return (T)_rootView.findViewById(viewId);
    }

    protected BaseActivity getBaseActivity(){
        return (BaseActivity)getActivity();
    }

    public Typeface getProhibitionRound(){
       return getBaseActivity().getProhibitionRound();
    }

    public void showProgress(){
        getBaseActivity().showProgress();
    }

    public void dismissProgress(){
        getBaseActivity().dismissProgress();
    }

    public void showAlert(String title, String content){
        getBaseActivity().showAlert(title, content);
    }
}
