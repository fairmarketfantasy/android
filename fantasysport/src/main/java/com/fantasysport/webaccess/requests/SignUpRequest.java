package com.fantasysport.webaccess.requests;

import android.net.Uri;
import com.fantasysport.models.AccessTokenData;
import com.fantasysport.models.User;
import com.fantasysport.models.UserData;
import com.fantasysport.webaccess.responses.AuthResponse;
import com.google.api.client.http.*;
import com.google.gson.Gson;

/**
 * Created by bylynka on 2/3/14.
 */

public class SignUpRequest extends BaseRequest<AuthResponse> {

    private SignUpRequestBody _requestBody;

    public SignUpRequest(User user) {
        super(AuthResponse.class);
        _requestBody = new SignUpRequestBody(user);
    }

    @Override
    public AuthResponse loadDataFromNetwork() throws Exception {
        UserData userData = signUp();
        User user = _requestBody.getUser();
        AccessTokenData accessTokenData = getAccessTokenData(user.getEmail(), user.getPassword());
        AuthResponse response = new AuthResponse(userData, accessTokenData);
        return  response;
    }


    public UserData signUp() throws Exception {
        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("users");
        String url = uriBuilder.build().toString();

        String js = new Gson().toJson(_requestBody);
        HttpContent content = ByteArrayContent.fromString("application/json", js);
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), content);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return new Gson().fromJson(result, UserData.class);
    }

    private AccessTokenData getAccessTokenData(String email, String password) throws Exception{
        AccessTokenRequestBody body = new AccessTokenRequestBody(email, password);
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