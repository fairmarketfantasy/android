package com.fantasysport.webaccess.requests;

import android.net.Uri;
import com.fantasysport.models.AccessTokenData;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;

public class AccessTokenRequest extends BaseRequest<AccessTokenData> {

    private AccessTokenRequestBody _body;

    public AccessTokenRequest(String email, String password) {
        super(AccessTokenData.class);
        _body = new AccessTokenRequestBody(email, password);
    }

    @Override
    public AccessTokenData loadDataFromNetwork() throws Exception {

        Uri.Builder uriBuilder = Uri.parse(getUrl()).buildUpon();
        uriBuilder.appendPath("oauth2");
        uriBuilder.appendPath("token");
        String url = uriBuilder.build().toString();
        String js = getObjectMapper().writeValueAsString(_body);
        HttpContent content = ByteArrayContent.fromString("application/json", js);
        HttpRequest request = getHttpRequestFactory()
                .buildPostRequest(new GenericUrl(url), content);
        request.getHeaders().setAccept("application/json");
        String result = request.execute().parseAsString();
        return getObjectMapper().readValue(result, this.getResultType());
    }
}