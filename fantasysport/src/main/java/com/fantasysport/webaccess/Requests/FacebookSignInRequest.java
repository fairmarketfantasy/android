package com.fantasysport.webaccess.Requests;

import android.net.Uri;
import com.fantasysport.models.UserData;

/**
 * Created by bylynka on 2/7/14.
 */
public class FacebookSignInRequest extends BaseRequest<UserData> {

    private FacebookSignInRequestBody _body;

    public FacebookSignInRequest(String fbAccessToken) {
        super(UserData.class);
        _body = new FacebookSignInRequestBody(fbAccessToken);
    }

    @Override
    public UserData loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("users").appendPath("auth")
                .appendPath("facebook_access_token")
                .appendPath("callback")
                .appendQueryParameter("access_token", _body.getAccessToken());
        String url = uriBuilder.build().toString();
        return getRestTemplate().getForObject(url, UserData.class);
    }
}
