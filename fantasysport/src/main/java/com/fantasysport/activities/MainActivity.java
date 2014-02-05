package com.fantasysport.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import com.fantasysport.R;
import com.fantasysport.models.User;
import com.fantasysport.webaccess.RequestListeners.RequestError;
import com.fantasysport.webaccess.RequestListeners.SignUpRequestListener;
import com.fantasysport.webaccess.Requests.SignUpRequest;
import com.fantasysport.webaccess.Requests.SignUpRequestBody;
import com.fantasysport.webaccess.Responses.SignUpResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octo.android.robospice.exception.NetworkException;
import com.octo.android.robospice.exception.RequestCancelledException;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = new User();
        user.setEmail("mytest2@gmail.com");
        user.setLogin("mylogin2");
        user.setRealName("my real name2");
        user.setPassword("123456");
        user.setPasswordConfirmation("123456");
        testSignUp(user);
    }


    private void testSignUp(User user){
        SignUpRequest request = new SignUpRequest(user);

        _spiceManager.execute(request, _signUpRequestListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    SignUpRequestListener _signUpRequestListener = new SignUpRequestListener() {
        @Override
        public void onRequestSuccess(SignUpResponse signUpResponse) {

        }

        @Override
        public void onRequestError(RequestError error) {

        }
    };

}
