package com.fantasysport.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.repo.Storage;
import com.fantasysport.webaccess.GsonGoogleHttpClientSpiceService;
import com.fantasysport.webaccess.WebProxy;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.requestListeners.SignOutResponseListener;
import com.octo.android.robospice.SpiceManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bylynka on 2/3/14.
 */
public class BaseActivity extends ActionBarActivity{

    protected SpiceManager _spiceManager = new SpiceManager(GsonGoogleHttpClientSpiceService.class);
    protected ProgressDialog _progress;
    protected WebProxy _webProxy;

    protected Handler _handler = new Handler();
    protected Storage _storage;
    protected static Typeface _prohibitionRoundTypeFace;
    protected static Typeface _robotoThin;

    @Override
    public void setContentView(int layoutResID) {
        _storage = Storage.instance();
        _webProxy = new WebProxy();
        _webProxy.setSpiceManager(_spiceManager);
        super.setContentView(layoutResID);
        initActionBar(getSupportActionBar());
    }

    public WebProxy getWebProxy(){
        return _webProxy;
    }

    @Override
    public void finish() {
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        super.finish();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    private void createProgressBar(){
        _progress = new ProgressDialog(this);
        _progress.setTitle(R.string.loading);
        _progress.setMessage(getString(R.string.wait_while_loading));
        _progress.setCancelable(false);
    }

    public void showProgress(){
        if(_progress == null){
            createProgressBar();
        }
        _progress.show();
    }

    public void dismissProgress(){
        if(_progress == null){
            return;
        }
        _progress.cancel();
        _progress.dismiss();
        _progress = null;
    }

    protected boolean isProgressShowing(){
        return _progress != null && _progress.isShowing();
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

    public void showAlert(String title, String message){
        showAlert(title, message, null);
    }

    public Typeface getProhibitionRound(){
        if(_prohibitionRoundTypeFace == null){
            _prohibitionRoundTypeFace  = Typeface.createFromAsset(getAssets(), "fonts/ProhibitionRound.ttf");
        }
        return _prohibitionRoundTypeFace;
    }

    public Typeface getRobotoThin(){
        if(_robotoThin == null){
            _robotoThin  = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        }
        return _robotoThin;
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

    protected void setHeaderText(String text){
        TextView textView = (TextView)getSupportActionBar().getCustomView().findViewById(R.id.fair_martet_txt);
        textView.setText(text);
    }

    protected void showWebView(String link, String header){
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(Const.WEB_LINK, link);
        intent.putExtra(Const.WEB_ACTIVITY_HEADER, header);
        startActivity(intent);
    }

    protected void showPredictions(){
        Intent intent = new Intent(this, PredictionActivity.class);
        startActivity(intent);
    }


    protected void showSettingsView(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    protected void signOut(){
        showProgress();
        _webProxy.signOut(new SignOutResponseListener() {
            @Override
            public void onRequestError(RequestError message) {
                dismissProgress();
                navigateToSignInScreen();
            }

            @Override
            public void onRequestSuccess(Object o) {
                dismissProgress();
                navigateToSignInScreen();
            }
        });
    }

    private void navigateToSignInScreen(){
        Intent intent = new Intent(this, SignInActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivityForResult(intent, Const.MAIN_ACTIVITY);
    }

    public boolean isEmailValid(String email) {
        if(email == null){
            return false;
        }
        email = email.trim();
        String regExp = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }
}
