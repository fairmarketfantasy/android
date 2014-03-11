package com.fantasysport.activities;

import android.content.Intent;
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
import com.fantasysport.webaccess.WebProxy;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.requestListeners.SignInResponseListener;
import com.fantasysport.webaccess.responses.AuthResponse;

public class SignInActivity extends AuthActivity {

    private final String STATE_EMAIL = "state_email";
    private final String STATE_PASSWORD= "state_password";

    private EditText _emailTxt;
    private EditText _passwordTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button facebookBtn = getViewById(R.id.facebook_btn);
        initFacebookAuth(facebookBtn);
        facebookBtn.setTypeface(getProhibitionRound());
        Button signInBtn = getViewById(R.id.sign_in_btn);
        signInBtn.setTypeface(getProhibitionRound());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        signInBtn.setOnClickListener(_signInBtnClickListener);
        TextView forgotPwdLbl = getViewById(R.id.forgot_pwd_lbl);
        forgotPwdLbl.setOnClickListener(_forgotPwdClickListener);

        setBackground();
        _emailTxt = getViewById(R.id.email_txt);
        _passwordTxt = getViewById(R.id.password_txt);
        _emailTxt.setOnEditorActionListener(_passwordTxtEditorActionListener);

        if (savedInstanceState != null) {
            _emailTxt.setText(savedInstanceState.getString(STATE_EMAIL));
            _passwordTxt.setText(savedInstanceState.getString(STATE_PASSWORD));
        }
    }

    private void attemptGetAccessToken(){
        String email = _emailTxt.getText().toString();
        String password = _passwordTxt.getText().toString();

        if (TextUtils.isEmpty(email)) {
            showAlert(null, getString(R.string.please_provide_email), null);
            return;
        }
        if (!isEmailValid(email)) {
            showAlert(null, getString(R.string.please_provide_valid_email), null);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showAlert(null, getString(R.string.please_provide_password), null);
            return;
        }
        showProgress();
        _webProxy.signIn(email, password, _userSignInResponseListener);
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

    SignInResponseListener _userSignInResponseListener = new SignInResponseListener() {
        @Override
        public void onRequestError(RequestError error) {
            dismissProgress();
            showAlert(getString(R.string.error), error.getMessage());
        }

        @Override
        public void onRequestSuccess(AuthResponse response) {
            _storage.setUserData(response.getUserData());
            loadMarkets();
        }
    };

    View.OnClickListener _signInBtnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            attemptGetAccessToken();
        }
    };

    View.OnClickListener _toSignUpListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
    };

    View.OnClickListener _forgotPwdClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showWebView("pages/mobile/forgot_password");
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_EMAIL, _emailTxt.getText().toString());
        outState.putString(STATE_PASSWORD, _passwordTxt.getText().toString());
        super.onSaveInstanceState(outState);
    }
}