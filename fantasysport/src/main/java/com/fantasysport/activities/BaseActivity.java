package com.fantasysport.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import com.fantasysport.webaccess.WLGsonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.fantasysport.R;

/**
 * Created by bylynka on 2/3/14.
 */
public class BaseActivity extends ActionBarActivity {

    protected SpiceManager _spiceManager = new SpiceManager(WLGsonSpringAndroidSpiceService.class);

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
}
