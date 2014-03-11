package com.fantasysport.webaccess.requests;

import android.net.Uri;
import com.fantasysport.Config;
import com.fantasysport.models.AccessTokenData;
import com.fantasysport.webaccess.RequestHelper;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.gson.Gson;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

public abstract class BaseRequest<T> extends GoogleHttpClientSpiceRequest<T> {

    private RequestHelper _rHelper;

    public BaseRequest(Class<T> clazz) {
        super(clazz);
        _rHelper = RequestHelper.instance();
    }

    protected String getUrl(){
        return Config.SERVER;
    }


    protected String getAccessToken(){
        return _rHelper.getAccessTokenData().getAccessToken();
    }

    public AccessTokenData getAccessTokenData(AccessTokenRequestBody body)throws Exception{
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
        AccessTokenData atData = new Gson().fromJson(result, AccessTokenData.class);
        RequestHelper.instance().setAccessTokenData(atData);
        return atData;
    }
}