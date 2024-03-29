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
import com.fantasysport.models.User;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.requestListeners.SignUpResponseListener;
import com.fantasysport.webaccess.WebProxy;
import com.fantasysport.webaccess.responses.AuthResponse;

public class SignUpActivity extends AuthActivity {

    private final String STATE_EMAIL = "state_email";
    private final String STATE_PASSWORD = "state_password";
    private final String STATE_NAME = "state_name";

    private EditText _emailTxt;
    private EditText _nameTxt;
    private EditText _passwordTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        Button facebookBtn = getViewById(R.id.facebook_btn);
        initFacebookAuth(facebookBtn);

        Button signUpBtn = getViewById(R.id.sign_up_btn);
        signUpBtn.setOnClickListener(_signUpBtnClickListener);

        _emailTxt = getViewById(R.id.email_txt);
        _nameTxt = getViewById(R.id.name_txt);
        _passwordTxt = getViewById(R.id.password_txt);
        _passwordTxt.setOnEditorActionListener(_passwordTxtEditorActionListener);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setBackground();

        if (savedInstanceState != null) {
            _emailTxt.setText(savedInstanceState.getString(STATE_EMAIL));
            _passwordTxt.setText(savedInstanceState.getString(STATE_PASSWORD));
            _nameTxt.setText(savedInstanceState.getString(STATE_NAME));
        }
    }

    private User getUser() {
        User user = new User();
        user.setEmail(_emailTxt.getText().toString());
        user.setLogin(_nameTxt.getText().toString());
        user.setRealName(_nameTxt.getText().toString());
        user.setPassword(_passwordTxt.getText().toString());
        user.setPasswordConfirmation(_passwordTxt.getText().toString());
        return user;
    }

    private void attemptSignUp() {
        String email = _emailTxt.getText().toString();
        String password = _passwordTxt.getText().toString();
        String name = _nameTxt.getText().toString();

        if (TextUtils.isEmpty(name)) {
            showAlert(null, getString(R.string.please_provide_name));
            return;
        }
        if (TextUtils.isEmpty(email)) {
            showAlert(null, getString(R.string.please_provide_email));
            return;
        }
        if (!isEmailValid(email)) {
            showAlert(null, getString(R.string.please_provide_valid_email));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showAlert(null, getString(R.string.please_provide_password));
            return;
        }

        if (password.length() < 6) {
            _passwordTxt.setError(getString(R.string.please_provide_valid_password));
            return;
        }

        showProgress();
        _webProxy.signUp(getUser(), _signUpResponseListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.auth, menu);
        MenuItem item = menu.findItem(R.id.action);
        new TextView(this);
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View titleView = inflator.inflate(R.layout.auth_menu_item, null);
        titleView.setOnClickListener(_toSingnInListener);
        TextView txt = (TextView)titleView.findViewById(R.id.item_txt);
        txt.setText(getString(R.string.sign_in));
        MenuItemCompat.setActionView(item, titleView);
        return super.onCreateOptionsMenu(menu);
    }


    TextView.OnEditorActionListener _passwordTxtEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptSignUp();
                return true;
            }
            return false;
        }
    };

    View.OnClickListener _signUpBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            attemptSignUp();
        }
    };

    View.OnClickListener _toSingnInListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
    };

    SignUpResponseListener _signUpResponseListener = new SignUpResponseListener() {

        @Override
        public void onRequestSuccess(AuthResponse response) {
            _storage.setUserData(response.getUserData());
            loadMarkets();
        }

        @Override
        public void onRequestError(RequestError error) {
            dismissProgress();
            showAlert(getString(R.string.error), error.getMessage());
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_EMAIL, _emailTxt.getText().toString());
        outState.putString(STATE_PASSWORD, _passwordTxt.getText().toString());
        outState.putString(STATE_NAME, _nameTxt.getText().toString());
        super.onSaveInstanceState(outState);
    }
}
