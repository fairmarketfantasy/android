package com.fantasysport.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import com.facebook.*;
import com.facebook.model.GraphObject;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.models.UserData;
import com.fantasysport.utility.image.ImageViewAnimatedChanger;
import com.fantasysport.webaccess.requestListeners.FaceBookAuthListener;
import com.fantasysport.webaccess.requestListeners.MarketsResponseListener;
import com.fantasysport.webaccess.requestListeners.RequestError;
import com.fantasysport.webaccess.responses.AuthResponse;
import com.fantasysport.webaccess.responses.MarketResponse;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bylynka on 2/10/14.
 */
public class AuthActivity extends BaseActivity {

    protected Bitmap _nbaBackground;
    protected Bitmap _mlbBackground;
    private Timer _backgroundImgTimer;

    protected void initFacebookAuth(Button facebookBtn) {
        facebookBtn.setOnClickListener(_facebookClickBntListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
        if(resultCode == Const.FINISH_ACTIVITY){
            finish();
        }
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
            } else if (SessionState.CLOSED == state) {
//                showAlert("Error", "Facebook error", null);
//                finishAuth();
            } else if (SessionState.CLOSED_LOGIN_FAILED == state){
                Session ses = Session.getActiveSession();
                if(ses != null){
                    session = new Session(AuthActivity.this);
                }
                Session.setActiveSession(session);
            }
        }
    };

    private void authByFacebook(Session session) {
        final String accessToken = session.getAccessToken();
        showProgress();
        Request request = Request.newGraphPathRequest(session, "me", new Request.Callback() {
            @Override
            public void onCompleted(Response response) {
                if(response.getError() != null){
                    dismissProgress();
                    finishAuth();
                    return;
                }
                GraphObject user = response.getGraphObject();
                String id = (String)user.getProperty("id");
                _webProxy.facebookLogin(accessToken, id, _userSignInResponseListener);
            }
        });
        request.executeAsync();
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
                BitmapDrawable nbaDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.nba_background);
                BitmapDrawable mlbDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.mlb_background);
                _nbaBackground = getBackground(nbaDrawable);
                _mlbBackground = getBackground(mlbDrawable);
                imgView.setImageBitmap(_nbaBackground);
            }
        });
    }




    private void startTimer(){
        stopTimer();
        final Handler handler = new Handler();
        TimerTask task = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        switchBackgroundImg();
                    }
                });

            }
        };
        _backgroundImgTimer = new Timer();
        _backgroundImgTimer.schedule(task, 3000, 3000);
    }

    Bitmap _current;
    protected void switchBackgroundImg(){
        if(_mlbBackground == null || _nbaBackground == null){
            return;
        }
        ImageView subImg = getViewById(R.id.sub_background_img);
        if(_current == null){
            _current = _nbaBackground;
        }
        if(_current == _nbaBackground){
            _current = _mlbBackground;
        }else {
            _current = _nbaBackground;
        }
        subImg.setImageBitmap(_current);
        ImageViewAnimatedChanger.changeImage(this, (ImageView) getViewById(R.id.background_img), _current);
    }

    private void stopTimer(){
        if(_backgroundImgTimer != null){
            _backgroundImgTimer.cancel();
            _backgroundImgTimer = null;
        }
    }

    protected Bitmap getBackground(BitmapDrawable bitmapDrawable){
        final ImageView imgView = getViewById(R.id.background_img);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        Bitmap newBitmap = null;
        int diffHeight = bitmap.getHeight() - imgView.getHeight();
        if (diffHeight > 0 && imgView.getHeight() != 0) {
            newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight() - diffHeight, null, false);
        } else {
            newBitmap = bitmap;
        }
        return newBitmap;
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    protected void onPause() {
        stopTimer();
        super.onPause();
    }

    protected void loadMarkets(){
        UserData data = _storage.getUserData();
        _webProxy.getMarkets(data.getCurrentSport(), _marketsResponseListener);
    }

    protected void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivityForResult(intent, Const.MAIN_ACTIVITY);
    }

    protected void startAuth(){}

    protected void finishAuth(){}

    MarketsResponseListener _marketsResponseListener = new MarketsResponseListener() {
        @Override
        public void onRequestError(RequestError error) {
            dismissProgress();
            showAlert(getString(R.string.error), error.getMessage());
            finishAuth();
        }

        @Override
        public void onRequestSuccess(MarketResponse response) {
            _storage.setDefaultRosterData(response.getDefaultRosterData());
            _storage.setMarketsContainer(response.getMarketsContainer());
            navigateToMainActivity();
            dismissProgress();
            finishAuth();
        }
    };

    FaceBookAuthListener _userSignInResponseListener = new FaceBookAuthListener() {
        @Override
        public void onRequestError(RequestError error) {
            dismissProgress();
            showAlert(getString(R.string.error), error.getMessage());
            finishAuth();
        }

        @Override
        public void onRequestSuccess(AuthResponse response) {
            _storage.setUserData(response.getUserData());
            loadMarkets();
        }
    };

    View.OnClickListener _facebookClickBntListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startAuth();
            authByFacebook2();
        }
    };
}