package com.fantasysport.webaccess;

import com.fantasysport.models.AccessTokenData;
import com.fantasysport.utility.DateUtils;

/**
 * Created by bylynka on 3/10/14.
 */
public class RequestHelper {

    private static RequestHelper _instance;

    private AccessTokenData _atData;
    private IListener _listener;

    public static RequestHelper instance() {
        if (_instance == null) {
            synchronized (RequestHelper.class) {
                if (_instance == null) {
                    _instance = new RequestHelper();
                }
            }
        }
        return _instance;
    }

    public boolean needRefresh(){
        long now = DateUtils.getCurrentDate().getTime();
        long aTokenTime = _atData.getCreateTime();
        long expireIn = _atData.getExpiresIn();
        return (now - aTokenTime >= (expireIn * 1000) - 15000);
    }

    public AccessTokenData getAccessTokenData(){
        return _atData;
    }

    public void setAccessTokenData(AccessTokenData data){
        _atData = data;
        raiseOnAccessTokenDataChanged();
    }

    public void setListener(IListener listener){
        _listener = listener;
    }

    private void raiseOnAccessTokenDataChanged(){
        if(_listener == null){
            return;
        }
        _listener.onAccessTokenDataChanged(_atData);
    }

    public interface IListener{
        public void onAccessTokenDataChanged(AccessTokenData data);
    }
}