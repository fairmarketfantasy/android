package com.fantasysport.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.repo.Storage;
import com.fantasysport.views.AlertDialog;
import com.fantasysport.webaccess.GsonGoogleHttpClientSpiceService;
import com.fantasysport.webaccess.WebProxy;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.requestListeners.SignOutResponseListener;
import com.octo.android.robospice.SpiceManager;
import net.hockeyapp.android.CrashManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bylynka on 2/3/14.
 */
public class BaseActivity extends ActionBarActivity{

    protected SpiceManager _spiceManager = new SpiceManager(GsonGoogleHttpClientSpiceService.class);
    protected WebProxy _webProxy;
    protected Storage _storage;
    protected int _progressCounter;
    private static final String APP_ID = "3b749930a830362f2e47565e66fec8b8";

    @Override
    protected void onResume() {
        super.onResume();
        checkForCrashes();
    }

    private void checkForCrashes() {
        CrashManager.register(this, APP_ID);
    }


    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public void setContentView(int layoutResID) {
        _storage = Storage.instance();
        _webProxy = new WebProxy();
        _webProxy.setSpiceManager(_spiceManager);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
//        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
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


    private void updateProgress(){
        if(_progressCounter < 0){
            _progressCounter = 0;
        }
        if(_progressCounter == 0){
            setSupportProgressBarIndeterminateVisibility(false);
            afterLoading();
        }else if(_progressCounter >= 1 && !isProgressShowing()){
            beforeLoading();
            setSupportProgressBarIndeterminateVisibility(true);
        }
    }

    protected void beforeLoading(){}

    protected void afterLoading(){}

    public void showProgress(){
        _progressCounter++;
        updateProgress();
    }

    public void dismissProgress(){
        _progressCounter--;
        updateProgress();
    }

    public boolean isProgressShowing(){
        return getSupportLoaderManager().hasRunningLoaders();
    }

    @Override
    protected void onStart() {
        super.onStart();
        _spiceManager.start(this);

    }

    @Override
    protected void onStop() {
        _progressCounter = 0;
        updateProgress();
        _spiceManager.shouldStop();
        super.onStop();
    }

    protected <T> T getViewById(int viewId){
        return (T)findViewById(viewId);
    }

    public void showAlert(String title, String message){
        new AlertDialog(this)
                .setTitle(title)
                .setContent(message)
                .show();
    }

    private void initActionBar(ActionBar bar){
        bar.setDisplayShowCustomEnabled(true);
        bar.setDisplayShowTitleEnabled(false);
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_CUSTOM);
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customBar = inflator.inflate(R.layout.action_bar_title, null);
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
        startActivityForResult(intent, Const.SETTINGS_ACTIVITY);
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
//        Intent intent = new Intent(this, SignInActivity.class);
////        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        startActivityForResult(intent, Const.MAIN_ACTIVITY);

        overridePendingTransition(R.anim.abc_fade_out, R.anim.abc_fade_in);
        super.finish();

//        finish();
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
