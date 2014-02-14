package com.fantasysport.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import com.facebook.Session;
import com.facebook.SessionState;
import com.fantasysport.R;
import com.fantasysport.Utility.UIConverter;
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
        final Session.OpenRequest request = new Session.OpenRequest(AuthActivity.this).setPermissions("basic_info", "email");
        request.setCallback(new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {

                        if (session.isOpened()) {
                            request.setCallback(null);
                            String accessToken = session.getAccessToken();
                            showProgress();
                            WebProxy.facebookLogin(accessToken, _spiceManager, _userUserDataResponseListener);
                        }else if(SessionState.CLOSED_LOGIN_FAILED == state || SessionState.CLOSED == state){
                            request.setCallback(null);
                            showErrorAlert("Error", "Facebook error", null);
                        }
            }
        });
        Session.setActiveSession(session);
        session.openForRead(request);
    }

    protected void setBackground(){
        final View barView = getActionBarView();
        ViewTreeObserver vto = barView.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    barView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    barView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int barViewHeight = barView.getHeight();
                ImageView img = getViewById(R.id.background_img);
                BitmapDrawable bitmapDrawable = ((BitmapDrawable) img.getDrawable());
                Bitmap bitmap = bitmapDrawable .getBitmap();
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
//                display.getSize(size);
                int height = display.getHeight();
//                int width = size.x;
//                int height = size.y;
//                Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight() - (UIConverter.dpiToPixel(65, SignInActivity.this)+barViewHeight), null, false);
                height = height <= 480? bitmap.getHeight():height;
                Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),height - (UIConverter.dpiToPixel(65, AuthActivity.this)+barViewHeight), null, false);
                img.setImageBitmap(newBitmap);
            }
        });
    }

    protected void navigateToMainActivity(){
        startActivity(new Intent(this, MainActivity.class));
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
            navigateToMainActivity();
        }
    };

    View.OnClickListener _facebookClickBntListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            authByFacebook();
        }
    };
}