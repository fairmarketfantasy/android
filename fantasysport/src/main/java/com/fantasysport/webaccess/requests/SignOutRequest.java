package com.fantasysport.webaccess.requests;

import android.net.Uri;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;

/**
 * Created by bylynka on 3/7/14.
 */
public class SignOutRequest extends BaseRequest<Object> {

    public SignOutRequest() {
        super(Object.class);
    }

    @Override
    public Void loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon()
                .appendPath("users")
                .appendPath("sign_out")
                .appendQueryParameter("access_token", getAccessToken());
        String url = uriBuilder.build().toString();
        HttpRequest request = getHttpRequestFactory().buildGetRequest(new GenericUrl(url));
        request.getHeaders().setAccept("application/json");
        _rHelper.signOut();
        return null;
    }
}
