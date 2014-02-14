package com.fantasysport.webaccess.Requests;

import android.net.Uri;
import com.fantasysport.webaccess.Responses.AccessTokenResponse;

/**
 * Created by bylynka on 2/13/14.
 */
public class AccessTokenRequest extends BaseRequest<AccessTokenResponse> {

    private AccessTokenRequestBody _body;

    public AccessTokenRequest(String email, String password) {
        super(AccessTokenResponse.class);
        _body = new AccessTokenRequestBody(email, password);
    }

    @Override
    public AccessTokenResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("oauth2");
        uriBuilder.appendPath("token");
        String url = uriBuilder.build().toString();
        return getRestTemplate().postForObject(url, _body, AccessTokenResponse.class);
    }
}