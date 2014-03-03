package com.fantasysport.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.fantasysport.R;
import com.fantasysport.models.Market;
import com.fantasysport.views.Switcher;
import com.fantasysport.webaccess.RequestListeners.MarketsResponseListener;
import com.fantasysport.webaccess.RequestListeners.RequestError;
import com.fantasysport.webaccess.RequestListeners.SignInResponseListener;
import com.fantasysport.webaccess.WebProxy;
import com.fantasysport.webaccess.responses.AuthResponse;

import java.util.List;

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

        TextView toSignUpLbl = getViewById(R.id.to_sign_up_lbl);
        toSignUpLbl.setOnClickListener(_toSignUpListener);

        setBackground();
        _emailTxt = getViewById(R.id.email_txt);
        _passwordTxt = getViewById(R.id.password_txt);
        _emailTxt.setOnEditorActionListener(_passwordTxtEditorActionListener);

        if (savedInstanceState != null) {
            _emailTxt.setText(savedInstanceState.getString(STATE_EMAIL));
            _passwordTxt.setText(savedInstanceState.getString(STATE_PASSWORD));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.auth, menu);
        MenuItem item = menu.findItem(R.id.action);
        new TextView(this);
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View titleView = inflator.inflate(R.layout.auth_menu_item, null);
        titleView.setOnClickListener(_toSignUpListener);

        TextView txt = (TextView)titleView.findViewById(R.id.item_txt);
        txt.setText(getString(R.string.sign_up));
        txt.setTypeface(getProhibitionRound());
        MenuItemCompat.setActionView(item, titleView);
        return super.onCreateOptionsMenu(menu);
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
        WebProxy.signIn(email, password, _spiceManager, _userSignInResponseListener);
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
            showErrorAlert(getString(R.string.error), error.getMessage());
        }

        @Override
        public void onRequestSuccess(AuthResponse response) {
            _storage.setUserData(response.getUserData());
            _storage.setAccessTokenData(response.getAccessTokenData());
            loadMarkets(_storage.getAccessTokenData().getAccessToken());
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_EMAIL, _emailTxt.getText().toString());
        outState.putString(STATE_PASSWORD, _passwordTxt.getText().toString());
        super.onSaveInstanceState(outState);
    }
}