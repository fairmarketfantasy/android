package com.fantasysport.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.fantasysport.R;
import com.fantasysport.repo.Storage;
import com.fantasysport.webaccess.GsonGoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;

/**
 * Created by bylynka on 2/3/14.
 */
public class BaseActivity extends ActionBarActivity{

    protected SpiceManager _spiceManager = new SpiceManager(GsonGoogleHttpClientSpiceService.class);
    protected ProgressDialog _progress;

    protected Handler _handler = new Handler();
    protected Storage _storage;
    protected Typeface _prohibitionRoundTypeFace;

    @Override
    public void setContentView(int layoutResID) {
        _storage = Storage.instance();
        super.setContentView(layoutResID);
        initActionBar(getSupportActionBar());
    }

    @Override
    public void finish() {
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        super.finish();
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

    protected void showAlert(String title, String message, DialogInterface.OnClickListener onCloseListener){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(R.string.close, onCloseListener)
                .show();
    }


    protected void showAlert(String title, String message){
        showAlert(title, message, null);
    }

    public Typeface getProhibitionRound(){
        if(_prohibitionRoundTypeFace == null){
            _prohibitionRoundTypeFace  = Typeface.createFromAsset(getAssets(), "fonts/ProhibitionRound.ttf");
        }
        return _prohibitionRoundTypeFace;
    }

    private void initActionBar(ActionBar bar){
        bar.setDisplayShowCustomEnabled(true);
        bar.setDisplayShowTitleEnabled(false);
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_CUSTOM);
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customBar = inflator.inflate(R.layout.action_bar_title, null);
        TextView textView = (TextView)customBar.findViewById(R.id.fair_martet_txt);
        textView.setTypeface(getProhibitionRound());
        getSupportActionBar().setCustomView(customBar);
    }
}
