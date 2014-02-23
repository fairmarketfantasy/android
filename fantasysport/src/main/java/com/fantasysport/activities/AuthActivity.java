package com.fantasysport.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.fantasysport.R;
import com.fantasysport.models.UserData;
import com.fantasysport.webaccess.RequestListeners.RequestError;
import com.fantasysport.webaccess.RequestListeners.UserDataResponseListener;
import com.fantasysport.webaccess.WebProxy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bylynka on 2/10/14.
 */
public class AuthActivity extends BaseActivity {

    protected void initFacebookAuth(Button facebookBtn) {
        facebookBtn.setOnClickListener(_facebookClickBntListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Session session = Session.getActiveSession();
        if (session == null) {
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, _fbSessionCallback, savedInstanceState);
            }
            if (session == null) {
                session = new Session(this);
            }
            Session.setActiveSession(session);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Session.getActiveSession().addCallback(_fbSessionCallback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Session.getActiveSession().removeCallback(_fbSessionCallback);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }

    private void authByFacebook2() {
        final Session.OpenRequest request = new Session.OpenRequest(AuthActivity.this).setPermissions("basic_info", "email").setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
        request.setCallback(_fbSessionCallback);
        Session session = Session.getActiveSession();
        if (!session.isOpened()) {
            session.openForRead(request);
        } else {
            Session.openActiveSession(this, true, _fbSessionCallback);
        }
    }

    Session.StatusCallback _fbSessionCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            if (session.isOpened()) {
                authByFacebook(session);
            } else if (SessionState.CLOSED_LOGIN_FAILED == state || SessionState.CLOSED == state) {
                showErrorAlert("Error", "Facebook error", null);
            }
        }
    };

    private void authByFacebook(Session session) {
        String accessToken = session.getAccessToken();
        showProgress();
        WebProxy.facebookLogin(accessToken, _spiceManager, _userUserDataResponseListener);
    }

    protected void setBackground() {
        final ImageView imgView = getViewById(R.id.background_img);
        ViewTreeObserver vto = imgView.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    imgView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    imgView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.basket);
                Bitmap bitmap = bitmapDrawable.getBitmap();
                Bitmap newBitmap = null;
                int diffHeight = bitmap.getHeight() - imgView.getHeight();
                if (diffHeight > 0 && imgView.getHeight() != 0) {
                    newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight() - diffHeight, null, false);
                } else {
                    newBitmap = bitmap;
                }
                imgView.setImageBitmap(newBitmap);
            }
        });
    }

    protected void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
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

    UserDataResponseListener _userUserDataResponseListener = new UserDataResponseListener() {
        @Override
        public void onRequestError(RequestError error) {
            dismissProgress();
            showErrorAlert(getString(R.string.error), error.getMessage());
        }

        @Override
        public void onRequestSuccess(UserData userData) {
            dismissProgress();
            _storage.setUserData(userData);
            navigateToMainActivity();
        }
    };

    View.OnClickListener _facebookClickBntListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            authByFacebook2();
        }
    };
}