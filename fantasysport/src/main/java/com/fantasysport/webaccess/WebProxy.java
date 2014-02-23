package com.fantasysport.webaccess;

import com.fantasysport.models.User;
import com.fantasysport.webaccess.RequestListeners.AccessTokenResponseListener;
import com.fantasysport.webaccess.RequestListeners.MarketsResponseListener;
import com.fantasysport.webaccess.RequestListeners.UserDataResponseListener;
import com.fantasysport.webaccess.Requests.*;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.retry.RetryPolicy;

/**
 * Created by bylynka on 2/6/14.
 */
public final class WebProxy {

    private WebProxy(){
    }

    public static void getGames(String gameType,String accessToken, SpiceManager spiceManager, MarketsResponseListener listener){
        GamesRequest request = new GamesRequest(gameType, accessToken);
        spiceManager.execute(request, listener);
    }

    public static void signIn(String accessToken, SpiceManager spiceManager, UserDataResponseListener listener){
        SignInRequest request = new SignInRequest(accessToken);
        spiceManager.execute(request, listener);
    }

    public static void getAccessToken(String email, String password, SpiceManager spiceManager, AccessTokenResponseListener listener){
        AccessTokenRequest request = new AccessTokenRequest(email, password);
        spiceManager.execute(request, listener);
    }

    public static void facebookLogin(String accessToken, SpiceManager spiceManager, UserDataResponseListener listener){
        FacebookSignInRequest request = new FacebookSignInRequest(accessToken);
        spiceManager.execute(request, listener);
    }


    public static void signUp(User user, SpiceManager spiceManager, UserDataResponseListener listener){
        SignUpRequest request = new SignUpRequest(user);
        request.setRetryPolicy(getRetryPolicy());
        spiceManager.execute(request, listener);
    }

    private static RetryPolicy getRetryPolicy(){
        return new RetryPolicy() {
            @Override
            public int getRetryCount() {
                return 0;
            }

            @Override
            public void retry(SpiceException e) {

            }

            @Override
            public long getDelayBeforeRetry() {
                return 0;
            }
        };
    }

}
