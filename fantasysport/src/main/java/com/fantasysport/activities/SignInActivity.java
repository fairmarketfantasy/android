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
import com.fantasysport.models.UserData;
import com.fantasysport.webaccess.RequestHelper;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.requestListeners.ResetPasswordResponse;
import com.fantasysport.webaccess.requestListeners.SignInResponseListener;
import com.fantasysport.webaccess.responses.AuthResponse;
import com.google.gson.Gson;

public class SignInActivity extends AuthActivity {

    private final String STATE_EMAIL = "state_email";
    private final String STATE_PASSWORD= "state_password";

    private EditText _emailTxt;
    private EditText _passwordTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        test();
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

//    private void test(){
//        try {
//
//
//        String str = "{\"id\":160,\"name\":\"Oleg Roberman\",\"admin\":false,\"username\":\"oroberman\",\"email\":\"oleg.roberman@gmail.com\",\"balance\":5000,\"image_url\":\"https://fairmarketfantasy-dev.s3.amazonaws.com/uploads/user/160/IMG_0481.JPG?AWSAccessKeyId=AKIAJXV4UPD3IV4JK6DA\\u0026Signature=KgHmSuZyh1KT4CTiYzAtlaRubyE%3D\\u0026Expires=1397121251\",\"win_percentile\":\"78.260869565217391304\",\"total_points\":2305,\"joined_at\":\"2014-01-31T18:42:27.256Z\",\"token_balance\":0,\"provider\":null,\"amount\":null,\"bets\":null,\"winnings\":null,\"total_wins\":18,\"total_losses\":5,\"bonuses\":{},\"referral_code\":\"68270ce995074de985e3e9089a2ace59\",\"inviter_id\":null,\"currentSport\":\"NBA\",\"in_progress_roster_id\":27255,\"abridged\":false,\"prestige\":526,\"confirmed\":true,\"customer_object\":{\"id\":153,\"balance\":5000,\"net_monthly_winnings\":\"-33024.0\",\"monthly_contest_entries\":48,\"contest_entries_deficit\":\"0.0\",\"locked\":false,\"locked_reason\":null,\"cards\":[],\"is_active\":true,\"has_agreed_terms\":true,\"contest_winnings_multiplier\":\"1.16512\",\"trial_started_at\":\"2014-03-11\",\"monthly_award\":\"384.76\"},\"in_progress_roster\":{\"id\":27255,\"owner_id\":160,\"owner_name\":\"oroberman\",\"state\":\"in_progress\",\"contest_id\":null,\"buy_in\":1500,\"remaining_salary\":\"100000.0\",\"score\":null,\"contest_rank\":null,\"contest_rank_payout\":0,\"amount_paid\":null,\"paid_at\":null,\"cancelled_cause\":null,\"cancelled_at\":null,\"positions\":\"PG,SG,PF,SF,C,G,F,UTIL\",\"started_at\":\"2014-04-10T23:55:00.000Z\",\"market_id\":20884,\"next_game_time\":null,\"live\":false,\"bonus_points\":0,\"perfect_score\":null,\"remove_benched\":true,\"view_code\":\"U_6zRQXBiIR-8zmDQNJnAg\",\"abridged\":false,\"league\":null,\"contest\":null,\"contest_type\":null,\"players\":[],\"market\":{\"id\":20884,\"name\":\"Spurs @ Mavericks\",\"shadow_bets\":\"337124.887409\",\"shadow_bet_rate\":\"0.75\",\"started_at\":\"2014-04-10T23:55:00.000Z\",\"opened_at\":\"2014-04-08T23:55:00.000Z\",\"closed_at\":\"2014-04-10T23:55:00.000Z\",\"sport_id\":867,\"total_bets\":\"275624.887409\",\"state\":\"opened\",\"market_duration\":\"day\",\"game_type\":\"regular_season\",\"games\":[{\"id\":\"c3fe7e85-3c5d-4995-8b19-e5c7564667e9\",\"stats_id\":\"c3fe7e85-3c5d-4995-8b19-e5c7564667e9\",\"status\":\"scheduled\",\"game_day\":\"2014-04-10\",\"game_time\":\"2014-04-11T00:00:00.000Z\",\"home_team\":\"Mavericks\",\"away_team\":\"Spurs\",\"season_type\":\"REG\",\"season_week\":0,\"season_year\":2013,\"network\":\"TNT\"}]}},\"leagues\":[]}";
//
//        UserData data = new Gson().fromJson(str, UserData.class);
//
//        }catch (Exception e){
//        String mes = e.getMessage();
//        }
//    }

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
            if(isProgressShowing()){
                return;
            }
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