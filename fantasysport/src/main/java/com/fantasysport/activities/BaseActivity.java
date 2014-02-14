package com.fantasysport.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.fantasysport.webaccess.WLGsonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.fantasysport.R;

/**
 * Created by bylynka on 2/3/14.
 */
public class BaseActivity extends FragmentActivity {

    protected SpiceManager _spiceManager = new SpiceManager(WLGsonSpringAndroidSpiceService.class);
    protected ProgressDialog _progress;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
//        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setActionBarFonts();
        DisplayMetrics dm = new DisplayMetrics();
    }


    private void createProgressBar(){
        _progress = new ProgressDialog(this);
        _progress.setTitle(R.string.loading);
        _progress.setMessage(getString(R.string.wait_while_loading));
        _progress.setCancelable(false);
    }

    protected void showProgress(){
        if(_progress == null){
            createProgressBar();
        }
        _progress.show();
    }

    protected void dismissProgress(){
        _progress.cancel();
        _progress.dismiss();
        _progress = null;
    }

    protected boolean isProgressShowing(){
        return _progress.isShowing();
    }

    @Override
    protected void onStart() {
        super.onStart();
        _spiceManager.start(this);

    }

    @Override
    protected void onStop() {
        _spiceManager.shouldStop();
        super.onStop();
    }

    protected <T> T getViewById(int viewId){
        return (T)findViewById(viewId);
    }

    protected void showErrorAlert(String title, String message, DialogInterface.OnClickListener onCloseListener){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(R.string.close, onCloseListener)
                .show();
    }

    protected void showErrorAlert(String title, String message){
        showErrorAlert(title, message, null);
    }

    protected View getActionBarView(){
        return  getViewById(R.id.includedLayout);
    }

    protected void setActionBarButtonText(String text){
        Button btn = getViewById(R.id.action_bar_btn);
        btn.setText(text);
    }

    protected void setActionBarFonts(){
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/ProhibitionRound.ttf");
        TextView textView = getViewById(R.id.fair_martet_txt);
        textView.setTypeface(tf);
        textView = getViewById(R.id.fantasy_txt);
        textView.setTypeface(tf);
        Button btn = getViewById(R.id.action_bar_btn);
        btn.setTypeface(tf);
    }
}
