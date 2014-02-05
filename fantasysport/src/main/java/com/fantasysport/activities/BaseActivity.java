package com.fantasysport.activities;

import android.support.v7.app.ActionBarActivity;
import com.octo.android.robospice.GsonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;

/**
 * Created by bylynka on 2/3/14.
 */
public class BaseActivity extends ActionBarActivity {

    protected SpiceManager _spiceManager = new SpiceManager(GsonSpringAndroidSpiceService.class);

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
}
