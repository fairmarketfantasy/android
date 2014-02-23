package com.fantasysport.webaccess.Requests;

import android.net.Uri;
import com.fantasysport.webaccess.Responses.AccessTokenResponse;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import com.octo.android.robospice.persistence.googlehttpclient.json.GsonObjectPersisterFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

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

        String js = new Gson().toJson(_body);
        HttpContent content = ByteArrayContent.fromString("application/json", js);
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), content);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return new Gson().fromJson(result, this.getResultType());
    }
}