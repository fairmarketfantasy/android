package com.fantasysport.webaccess.Requests;

import android.net.Uri;
import com.fantasysport.models.UserData;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;

/**
 * Created by bylynka on 2/13/14.
 */
//public class SignInRequest extends BaseRequest<UserData> {
//
//    private String _accessToken;
//
//    public SignInRequest(String accessToken) {
//        super(UserData.class);
//        _accessToken = accessToken;
//    }
//
//    @Override
//    public UserData loadDataFromNetwork() throws Exception {
//        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
//        uriBuilder.appendPath("users")
//                .appendPath("sign_in")
//                .appendQueryParameter("access_token", _accessToken);
//        String url = uriBuilder.build().toString();
//        return getRestTemplate().postForObject(url, null, UserData.class);
//    }
//}

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

        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), null);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return new Gson().fromJson(result, this.getResultType());
    }
}
