package com.fantasysport.webaccess.Requests;

import android.net.Uri;
import com.fantasysport.models.UserData;

/**
 * Created by bylynka on 2/13/14.
 */
public class SignInRequest extends BaseRequest<UserData> {

    private String _accessToken;

    public SignInRequest(String accessToken) {
        super(UserData.class);
        _accessToken = accessToken;
    }

    @Override
    public UserData loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("users")
                .appendPath("sign_in")
                .appendQueryParameter("access_token", _accessToken);
        String url = uriBuilder.build().toString();
        return getRestTemplate().postForObject(url, null, UserData.class);
    }
}
