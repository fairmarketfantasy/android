package com.fantasysport.webaccess.Requests;

import android.net.Uri;
import com.fantasysport.models.UserData;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;

/**
 * Created by bylynka on 2/7/14.
 */
//public class FacebookSignInRequest extends BaseRequest<UserData> {
//
//    private FacebookSignInRequestBody _body;
//
//    public FacebookSignInRequest(String fbAccessToken) {
//        super(UserData.class);
//        _body = new FacebookSignInRequestBody(fbAccessToken);
//    }
//
//    @Override
//    public UserData loadDataFromNetwork() throws Exception {
//        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
//        uriBuilder.appendPath("users").appendPath("auth")
//                .appendPath("facebook_access_token")
//                .appendPath("callback")
//                .appendQueryParameter("access_token", _body.getAccessToken());
//        String url = uriBuilder.build().toString();
//        return getRestTemplate().getForObject(url, UserData.class);
//    }
//}

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

        HttpRequest request = getHttpRequestFactory()
                .buildGetRequest(new GenericUrl(url));
        String result = request.execute().parseAsString();
        return new Gson().fromJson(result, this.getResultType());
    }
}
