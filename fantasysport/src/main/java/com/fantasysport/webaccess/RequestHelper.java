package com.fantasysport.webaccess;

import com.fantasysport.models.AccessTokenData;
import com.fantasysport.models.DefaultRosterData;
import com.fantasysport.models.Market;
import com.fantasysport.models.UserData;
import com.fantasysport.utility.DateUtils;

import java.util.List;

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

    private void raiseOnSignOut(){
        if(_listener == null){
            return;
        }
        _listener.onSignOut();
    }

    private void raiseOnMarketsLoaded(List<Market> markets){
        if(_listener == null){
            return;
        }
        _listener.onMarkets(markets);
    }

    private void raiseOnDefaultRosterData(DefaultRosterData data){
        if(_listener == null){
            return;
        }
        _listener.onDefaultRosterData(data);
    }

    private void raiseOnUserData(UserData data){
        if(_listener == null){
            return;
        }
        _listener.onUserData(data);
    }

    public void loadDefaultRosterData(DefaultRosterData data){
        raiseOnDefaultRosterData(data);
    }

    public void loadMarkets(List<Market> markets){
        raiseOnMarketsLoaded(markets);
    }

    public void loadUserData(UserData data){
        raiseOnUserData(data);
    }

    public void signOut(){
        raiseOnSignOut();
    }

    public interface IListener{
        public void onAccessTokenDataChanged(AccessTokenData data);
        public void onSignOut();
        public void onMarkets(List<Market> markets);
        public void onDefaultRosterData(DefaultRosterData data);
        public void onUserData(UserData data);
    }
}