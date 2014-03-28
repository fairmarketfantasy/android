package com.fantasysport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.fantasysport.R;
import com.fantasysport.webaccess.RequestHelper;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.requestListeners.ResetPasswordResponse;
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
        if(RequestHelper.instance().getAccessTokenData() != null){
            navigateToMainActivity();
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
            startActivity(intent);
        }
    };

    View.OnClickListener _forgotPwdClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = layoutInflater.inflate(R.layout.popup_forgot_password, null);
            TextView headerLbl = (TextView)popupView.findViewById(R.id.forgot_pwd_lbl);
            headerLbl.setTypeface(getProhibitionRound());
            Button submit = (Button)popupView.findViewById(R.id.submit_btn);
            submit.setTypeface(getProhibitionRound());
            final EditText mailTxt = (EditText)popupView.findViewById(R.id.email_txt);
            View cancelView = popupView.findViewById(R.id.cancel_btn);
            final TextView errorLbl = (TextView)popupView.findViewById(R.id.error_lbl);
            final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            cancelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mail = mailTxt.getText().toString();
                    if(isEmailValid(mail)){
                        popupWindow.dismiss();
                        sendRestorePasswordInstruction(mail);
                    }else {
                        errorLbl.setVisibility(View.VISIBLE);
                    }
                }
            });
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        }
    };

    private void sendRestorePasswordInstruction(String email){
        _webProxy.resetPassword(email, new ResetPasswordResponse() {
            @Override
            public void onRequestError(RequestError message) {

            }

            @Override
            public void onRequestSuccess(Object o) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_EMAIL, _emailTxt.getText().toString());
        outState.putString(STATE_PASSWORD, _passwordTxt.getText().toString());
        super.onSaveInstanceState(outState);
    }


}