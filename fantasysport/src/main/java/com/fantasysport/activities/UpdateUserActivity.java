package com.fantasysport.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.adapters.SettingsItemEnum;
import com.fantasysport.models.User;
import com.fantasysport.models.UserData;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.requestListeners.UpdateUserResponseListener;

/**
 * Created by bylynka on 3/11/14.
 */
public class UpdateUserActivity extends BaseActivity {

    private SettingsItemEnum _settingsItem;
    private EditText _contentTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setInitParams(savedInstanceState);
        Button submitBtn = getViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(_submitClickListener);
        setContent();
    }

    protected void setContent(){
        TextView headerLbl = getViewById(R.id.header_lbl);
        _contentTxt = getViewById(R.id.text_txt);
        View confirmPwdView = getViewById(R.id.confirm_pwd_block);
        View currentPwdView = getViewById(R.id.current_pwd_block);

        switch (_settingsItem){
            case Name:
                headerLbl.setText(getString(R.string.change_name));
                _contentTxt.setHint(getString(R.string.new_name));
                confirmPwdView.setVisibility(View.GONE);
                currentPwdView.setVisibility(View.GONE);
                break;
            case Email:
                headerLbl.setText(getString(R.string.change_email));
                _contentTxt.setHint(getString(R.string.new_email));
                _contentTxt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                confirmPwdView.setVisibility(View.GONE);
                currentPwdView.setVisibility(View.GONE);
                break;
            case Password:
                headerLbl.setText(getString(R.string.change_password));
                _contentTxt.setHint(getString(R.string.new_pwd));
                _contentTxt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                confirmPwdView.setVisibility(View.VISIBLE);
                currentPwdView.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setInitParams(Bundle savedInstanceState){
        if(savedInstanceState == null){
            Intent intent = getIntent();
            _settingsItem = (SettingsItemEnum)intent.getSerializableExtra(Const.SETTINGS_ITEM_ENUM);
        }else{
            _settingsItem = (SettingsItemEnum)savedInstanceState.getSerializable(Const.SETTINGS_ITEM_ENUM);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Const.SETTINGS_ITEM_ENUM, _settingsItem);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener _submitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (_settingsItem){
                case Name:
                    updateName();
                    break;
                case Email:
                    updateEmail();
                    break;
                case Password:
                    updatePassword();
                    break;
            }
        }
    };

    private void updateName(){
        String name = _contentTxt.getText().toString();
        if (TextUtils.isEmpty(name)) {
            showAlert(null, getString(R.string.please_provide_name), null);
            return;
        }
        User user = getCurrentUser();
        user.setRealName(name);
        updateUser(user);
    }

    private void updateEmail(){
        String email = _contentTxt.getText().toString();
        if (!isEmailValid(email)) {
            showAlert(getString(R.string.error), getString(R.string.please_provide_valid_email));
            return;
        }
        User user = getCurrentUser();
        user.setEmail(email);
        updateUser(user);
    }

    private void updatePassword(){
        EditText currentPwdTxt = getViewById(R.id.current_password_txt);
        EditText confirmPwdTxt = getViewById(R.id.confirm_pwd_txt);
        String currentPwd = currentPwdTxt.getText().toString();
        String newPwd = _contentTxt.getText().toString();
        String confirmPwd = confirmPwdTxt.getText().toString();

        if (currentPwd.length() < 6) {
            showAlert(getString(R.string.error), getString(R.string.please_provide_valid_current_password));
            return;
        }
        if(newPwd.length() < 6){
            showAlert(getString(R.string.error), getString(R.string.please_provide_valid_password));
            return;
        }
        if(newPwd.compareTo(confirmPwd) != 0){
            showAlert(getString(R.string.error), getString(R.string.please_provide_valid_confirm_password));
            return;
        }

        User user = getCurrentUser();
        user.setPassword(newPwd);
        user.setPasswordConfirmation(confirmPwd);
        user.setCurrentPassword(currentPwd);
        updateUser(user);
    }

    private void updateUser(User user){
        showProgress();
        _webProxy.updateUser(user, new UpdateUserResponseListener() {
            @Override
            public void onRequestError(RequestError error) {
                dismissProgress();
                showAlert(getString(R.string.error), error.getMessage());
            }

            @Override
            public void onRequestSuccess(UserData userData) {
                _storage.setUserData(userData);
                dismissProgress();
                finish();
            }
        });
    }

    private User getCurrentUser(){
        User user = new User();
        UserData data = _storage.getUserData();
        user.setRealName(data.getRealName());
        user.setId(data.getId());
        user.setEmail(data.getEmail());
        return user;
    }
}
