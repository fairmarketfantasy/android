package com.fantasysport.webaccess;

import com.fantasysport.models.User;
import com.fantasysport.webaccess.RequestListeners.SignUpRequestListener;
import com.fantasysport.webaccess.Requests.FacebookSignInRequest;
import com.fantasysport.webaccess.Requests.SignUpRequest;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.retry.DefaultRetryPolicy;
import com.octo.android.robospice.retry.RetryPolicy;

/**
 * Created by bylynka on 2/6/14.
 */
public final class WebProxy {

    private WebProxy(){
    }

    public static void facebookLogin(String accessToken, SpiceManager spiceManager){
        FacebookSignInRequest request = new FacebookSignInRequest(accessToken);
        spiceManager.execute(request, null);
    }

    public static void signUp(User user, SpiceManager spiceManager, SignUpRequestListener listener){
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



//    public static void signUp(User user){
//        final String json = new Gson().toJson(new SignUpRequestBody(user));
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//
//                    String response = RequestSender.makeRequest("http://192.168.88.78:3000/users", json, RequestSender.METHOD_POST, null);
//                    Log.i("---------response---------", response);
//                }catch (Exception e){
//                    Object o = e.toString();
//                }
//
//            }
//        }).start();
//
//    }
