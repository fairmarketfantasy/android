package com.fantasysport.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.fantasysport.R;
import com.fantasysport.models.UserData;
import com.fantasysport.webaccess.RequestListeners.AccessTokenResponseListener;
import com.fantasysport.webaccess.RequestListeners.RequestError;
import com.fantasysport.webaccess.RequestListeners.UserDataResponseListener;
import com.fantasysport.webaccess.Responses.AccessTokenResponse;
import com.fantasysport.webaccess.WebProxy;

public class SignInActivity extends AuthActivity {

    private final String STATE_EMAIL = "state_email";
    private final String STATE_PASSWORD= "state_password";

    private EditText _emailTxt;
    private EditText _passwordTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        setActionBarButtonText(getString(R.string.sign_up));
        Button toSignUpBtn = getViewById(R.id.action_bar_btn);
        toSignUpBtn.setOnClickListener(_toSignUpBtnClickListener);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/ProhibitionRound.ttf");
        Button facebookBtn = getViewById(R.id.facebook_btn);
        initFacebookAuth(facebookBtn);
        facebookBtn.setTypeface(tf);
        Button signInBtn = getViewById(R.id.sign_in_btn);
        signInBtn.setTypeface(tf);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        signInBtn.setOnClickListener(_signInBtnClickListener);
        setBackground();
        _emailTxt = getViewById(R.id.email_txt);
        _passwordTxt = getViewById(R.id.password_txt);
        _emailTxt.setOnEditorActionListener(_passwordTxtEditorActionListener);

        if (savedInstanceState != null) {
            _emailTxt.setText(savedInstanceState.getString(STATE_EMAIL));
            _passwordTxt.setText(savedInstanceState.getString(STATE_PASSWORD));
        }

    }

    private void attemptSignIn(String accessToken) {
        if(!isProgressShowing()){
            showProgress();
        }
        WebProxy.signIn(accessToken, _spiceManager, _userUserDataResponseListener);
    }

    private void attemptGetAccessToken(){
        String email = _emailTxt.getText().toString();
        String password = _passwordTxt.getText().toString();

        if (TextUtils.isEmpty(email)) {
            showErrorAlert(null, getString(R.string.please_provide_email), null);
            return;
        }
        if (!isEmailValid(email)) {
            showErrorAlert(null, getString(R.string.please_provide_valid_email), null);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showErrorAlert(null, getString(R.string.please_provide_password), null);
            return;
        }
        showProgress();
        WebProxy.getAccessToken(email, password, _spiceManager, _accessTokenRequestListener);
    }

    TextView.OnEditorActionListener _passwordTxtEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptGetAccessToken();
                return true;
            }
            return false;
        }
    };

    UserDataResponseListener _userUserDataResponseListener = new UserDataResponseListener() {
        @Override
        public void onRequestError(RequestError error) {
            dismissProgress();
            showErrorAlert(getString(R.string.error), error.getMessage());
        }

        @Override
        public void onRequestSuccess(UserData userData) {
            dismissProgress();
            navigateToMainActivity();
        }
    };

    AccessTokenResponseListener _accessTokenRequestListener = new AccessTokenResponseListener() {
        @Override
        public void onRequestError(RequestError error) {
            dismissProgress();
            showErrorAlert(getString(R.string.error), error.getMessage());
        }

        @Override
        public void onRequestSuccess(AccessTokenResponse accessTokenResponse) {
            attemptSignIn(accessTokenResponse.getAccessToken());
        }
    };

    View.OnClickListener _signInBtnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            attemptGetAccessToken();
        }
    };

    View.OnClickListener _toSignUpBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_EMAIL, _emailTxt.getText().toString());
        outState.putString(STATE_PASSWORD, _passwordTxt.getText().toString());
        super.onSaveInstanceState(outState);
    }
}
