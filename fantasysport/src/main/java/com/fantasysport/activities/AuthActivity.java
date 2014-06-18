package com.fantasysport.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import com.facebook.*;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.fantasysport.Const;
import com.fantasysport.R;
import com.fantasysport.fragments.main.FragmentDataLoader;
import com.fantasysport.utility.TypefaceProvider;
import com.fantasysport.utility.image.ImageViewAnimatedChanger;
import com.fantasysport.webaccess.responseListeners.FaceBookAuthListener;
import com.fantasysport.webaccess.responseListeners.RequestError;
import com.fantasysport.webaccess.responses.AuthResponse;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bylynka on 2/10/14.
 */
public class AuthActivity extends BaseActivity {

    protected Bitmap _nbaBackground;
    protected Bitmap _mlbBackground;
    private Timer _backgroundImgTimer;
    private UiLifecycleHelper _uiHelper;

    protected void initFacebookAuth(LoginButton facebookBtn) {
//        facebookBtn.setOnClickListener(_facebookClickBntListener);
        facebookBtn.setReadPermissions(Arrays.asList("email", "basic_info"));
        facebookBtn.setTypeface(TypefaceProvider.getProhibitionRound(this));
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        _uiHelper.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Const.FINISH_ACTIVITY){
            finish();
        }
        switch (resultCode){
            case Const.SIGN_OUT:
                Session s = Session.getActiveSession();
                if(s != null && s.isOpened()){
                    s.closeAndClearTokenInformation();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Session session = Session.getActiveSession();
        _uiHelper = new UiLifecycleHelper(this, _fbSessionCallback);
        _uiHelper.onCreate(savedInstanceState);
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
        finishAuth();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        _uiHelper.onSaveInstanceState(outState);
    }


    private void onSessionStateChange(Session session, SessionState state,
                                    Exception exception) {

        session = Session.getActiveSession();
        SharedPreferences storedPrefs = PreferenceManager
                .getDefaultSharedPreferences(AuthActivity.this.getApplicationContext());
        SharedPreferences.Editor editor = storedPrefs.edit();
        editor.putBoolean("userLoggedTracker", true);
        editor.commit();

        if (state.isOpened()) {
            Log.i("AuthActivity", "Logged in...");
            makeMeRequest(session);
            editor.putBoolean("userLoggedTracker", false);
            editor.commit();
//                getView().setVisibility(View.GONE);

        } else if (state.isClosed()) {
            Log.i("AuthActivity", "Logged out...");
            editor.putBoolean("userLoggedTracker", true);
            editor.commit();
//                getView().setVisibility(View.VISIBLE);
        }

    }


    Session.StatusCallback _fbSessionCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private void makeMeRequest(final Session session) {
        Request request = Request.newMeRequest(session,
                new Request.GraphUserCallback() {
                    @Override
                    public void onCompleted(GraphUser user, Response response) {

                        // If the response is successful
                        if (session == Session.getActiveSession()) {
                            authByFacebook(session);
                        }
                        if (response.getError() != null) {
                            showAlert(getString(R.string.error), response.getError().getErrorMessage());
                        }
                    }
                });
        request.executeAsync();
    }

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

//        Session session = Session.getActiveSession();
//        if (session != null && (session.isOpened() || session.isClosed())) {
//            onSessionStateChange(session, session.getState(), null);
//        }
        _uiHelper.onResume();
    }

    @Override
    protected void onPause() {
        stopTimer();
        super.onPause();
        _uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _uiHelper.onDestroy();
    }

    protected void navigateToMainActivity(int category_type) {
        finishAuth();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Const.CATEGORY_TYPE, category_type);
        startActivityForResult(intent, Const.MAIN_ACTIVITY);
    }

    protected void startAuth(){}

    protected void finishAuth(){}

    FaceBookAuthListener _userSignInResponseListener = new FaceBookAuthListener() {
        @Override
        public void onRequestError(RequestError error) {
            dismissProgress();
            showAlert(getString(R.string.error), error.getMessage());
            finishAuth();
        }

        @Override
        public synchronized void onRequestSuccess(AuthResponse response) {
            _storage.setUserData(response.getUserData());
            final int category_type = _storage.getCategoryType();
            FragmentDataLoader.load(_webProxy, _storage, new FragmentDataLoader.ILoadedDataListener() {
                @Override
                public void onLoaded(RequestError error) {
                    if (error != null) {
                        dismissProgress();
                        finishAuth();
                        showAlert(getString(R.string.error), error.getMessage());

                    } else {
                        navigateToMainActivity(category_type);
                    }
                }
            });
        }
    };
}