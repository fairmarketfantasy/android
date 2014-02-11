package com.fantasysport.activities;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import com.facebook.Session;
import com.facebook.SessionState;
import com.fantasysport.webaccess.WebProxy;

/**
 * Created by bylynka on 2/10/14.
 */
public class FacebookAuthActivity extends BaseActivity {

    protected void initFacebookAuth(Button facebookBtn){
        facebookBtn.setOnClickListener(_facebookClickBntListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    private void authByFacebook(){
        Session session = new Session(this);
        final Session.OpenRequest request = new Session.OpenRequest(FacebookAuthActivity.this).setPermissions("basic_info", "email");
        request.setCallback(new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {

                        if (session.isOpened()) {
                            request.setCallback(null);
                            String accessToken = session.getAccessToken();
                            WebProxy.facebookLogin(accessToken, _spiceManager);
                        }else if(SessionState.CLOSED_LOGIN_FAILED == state || SessionState.CLOSED == state){
                            request.setCallback(null);
                            showErrorAlert("Error", "Facebook error", null);
                        }
            }
        });
        Session.setActiveSession(session);
        session.openForRead(request);
    }

    View.OnClickListener _facebookClickBntListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            authByFacebook();
        }
    };
}