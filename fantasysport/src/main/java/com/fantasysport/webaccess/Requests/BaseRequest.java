package com.fantasysport.webaccess.Requests;

import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

/**
 * Created by bylynka on 2/3/14.
 */
public abstract class BaseRequest<T> extends SpringAndroidSpiceRequest<T> {

    public BaseRequest(Class<T> clazz) {
        super(clazz);
    }

    protected String getUrl(){
//        https://fairmarketfantasy.com/
//        http://192.168.88.78:3000/
        return "http://192.168.88.78:3000/";
    }
}