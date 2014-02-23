package com.fantasysport.webaccess.Requests;

import com.fantasysport.Config;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

public abstract class BaseRequest<T> extends GoogleHttpClientSpiceRequest<T> {

    public BaseRequest(Class<T> clazz) {
        super(clazz);
    }

    protected String getUrl(){
        return Config.SERVER;
    }
}