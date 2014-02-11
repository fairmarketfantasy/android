package com.fantasysport.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.fantasysport.R;
import com.fantasysport.webaccess.RequestListeners.RequestError;
import com.fantasysport.webaccess.RequestListeners.SignUpRequestListener;
import com.fantasysport.webaccess.Responses.SignUpResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SignInActivity extends FacebookAuthActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button toSignUpBtn = getViewById(R.id.to_sign_up_btn);
        toSignUpBtn.setOnClickListener(_toSignUpBtnClickListener);

        Button facebookBtn = getViewById(R.id.facebookBtn);
        initFacebookAuth(facebookBtn);

//        button.setReadPermissions(Arrays.asList("email"));

//        Button signUpBtn = (Button)findViewById(R.id.sign_in_button);
//        signUpBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                User user = new User();
//                EditText editText = (EditText)findViewById(R.id.email_sign_in);
//                user.setEmail(editText.getText().toString());
//                editText = (EditText)findViewById(R.id.login_sign_in);
//                user.setLogin(editText.getText().toString());
//                editText = (EditText)findViewById(R.id.real_name_sign_up);
//                user.setRealName(editText.getText().toString());
//                editText = (EditText)findViewById(R.id.password_sign_up);
//                user.setPassword(editText.getText().toString());
//                user.setPasswordConfirmation(editText.getText().toString());
//                testSignUp(user);
//                WebProxy.signUp(user);
//                SimpleTextRequest request = new SimpleTextRequest("http://192.168.88.78:3000");

//            }
//        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    View.OnClickListener _toSignUpBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
    };
}
