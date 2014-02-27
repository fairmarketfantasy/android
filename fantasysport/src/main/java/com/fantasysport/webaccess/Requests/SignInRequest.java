package com.fantasysport.webaccess.Requests;

import android.net.Uri;
import com.fantasysport.models.AccessTokenData;
import com.fantasysport.models.UserData;
import com.fantasysport.webaccess.responses.AuthResponse;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;


public class SignInRequest extends BaseRequest<AuthResponse> {

    private String _email;
    private String _password;

    public SignInRequest(String email, String password) {
        super(AuthResponse.class);
        _email = email;
        _password = password;
    }

    @Override
    public AuthResponse loadDataFromNetwork() throws Exception {

        AccessTokenData accessTokenData = getAccessTokenData();
        UserData userData = getUserData(accessTokenData.getAccessToken());
        AuthResponse response = new AuthResponse(userData, accessTokenData);
        return response;
    }

    private UserData getUserData(String accessToken) throws Exception{
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("users")
                .appendPath("sign_in")
                .appendQueryParameter("access_token", accessToken);

        String url = uriBuilder.build().toString();

        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), null);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return new Gson().fromJson(result, UserData.class);
    }

    private AccessTokenData getAccessTokenData() throws Exception{
        AccessTokenRequestBody body = new AccessTokenRequestBody(_email, _password);
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
