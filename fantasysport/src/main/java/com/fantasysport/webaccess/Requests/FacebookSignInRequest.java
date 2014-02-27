package com.fantasysport.webaccess.Requests;

import android.net.Uri;
import com.fantasysport.models.AccessTokenData;
import com.fantasysport.models.UserData;
import com.fantasysport.webaccess.responses.AuthResponse;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;

/**
 * Created by bylynka on 2/7/14.
 */

public class FacebookSignInRequest extends BaseRequest<AuthResponse> {

    private FacebookSignInRequestBody _body;
    private String _uid;

    public FacebookSignInRequest(String fbAccessToken, String uid) {
        super(AuthResponse.class);
        _body = new FacebookSignInRequestBody(fbAccessToken);
        _uid = uid;
    }

    @Override
    public AuthResponse loadDataFromNetwork() throws Exception {
        UserData userData = facebookAuth();
        AccessTokenData accessTokenData = getAccessTokenData(_body.getAccessToken());
        return new AuthResponse(userData, accessTokenData);
    }

    public UserData facebookAuth() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("users").appendPath("auth")
                .appendPath("facebook_access_token")
                .appendPath("callback")
                .appendQueryParameter("access_token", _body.getAccessToken());
        String url = uriBuilder.build().toString();

        HttpRequest request = getHttpRequestFactory()
                .buildGetRequest(new GenericUrl(url));
        String result = request.execute().parseAsString();
        return new Gson().fromJson(result, UserData.class);
    }

    private AccessTokenData getAccessTokenData(String accessToken) throws Exception{
        AccessTokenRequestBody body = new AccessTokenRequestBody();
        body.setFacebookAuth(accessToken, _uid);
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("oauth2");
        uriBuilder.appendPath("token");
        String url = uriBuilder.build().toString();
        String js = new Gson().toJson(body);
        HttpContent content = ByteArrayContent.fromString("application/json", js);
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), content);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return new Gson().fromJson(result, AccessTokenData.class);
    }
}
