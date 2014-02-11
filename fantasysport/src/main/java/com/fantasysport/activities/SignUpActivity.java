package com.fantasysport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.fantasysport.R;
import com.fantasysport.models.User;
import com.fantasysport.webaccess.RequestListeners.RequestError;
import com.fantasysport.webaccess.RequestListeners.SignUpRequestListener;
import com.fantasysport.webaccess.Responses.SignUpResponse;
import com.fantasysport.webaccess.WebProxy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends FacebookAuthActivity {

    private EditText _emailTxt;
    private EditText _nameTxt;
    private EditText _passwordTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        Button toSignUpBtn = getViewById(R.id.to_sign_in_btn);
        toSignUpBtn.setOnClickListener(_toSignInBtnClickListener);

        Button facebookBtn = getViewById(R.id.facebook_btn);
        initFacebookAuth(facebookBtn);

        Button signUpBtn = getViewById(R.id.sign_up_btn);
        signUpBtn.setOnClickListener(_signUpBtnClickListener);

        _emailTxt = getViewById(R.id.email_txt);
        _nameTxt = getViewById(R.id.name_txt);
        _passwordTxt = getViewById(R.id.password_txt);
        _passwordTxt.setOnEditorActionListener(_passwordTxtEditorActionListener);
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
        String password = _passwordTxt.toString();
        String name = _nameTxt.toString();

        if (TextUtils.isEmpty(name)) {
            showErrorAlert(null, getString(R.string.please_provide_name), null);
            return;
        }
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

        if (password.length() < 6) {
            _passwordTxt.setError(getString(R.string.please_provide_valid_password));
            return;
        }

        WebProxy.signUp(getUser(), _spiceManager, _signUpRequestListener);
    }

    public boolean isEmailValid(String email) {
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

    View.OnClickListener _toSignInBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
    };

    SignUpRequestListener _signUpRequestListener = new SignUpRequestListener() {

        @Override
        public void onRequestSuccess(SignUpResponse signUpResponse) {
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }

        @Override
        public void onRequestError(RequestError error) {
            showErrorAlert(getString(R.string.error), error.getMessage());
        }
    };
}
