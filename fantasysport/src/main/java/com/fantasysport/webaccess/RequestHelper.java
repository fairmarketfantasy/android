package com.fantasysport.webaccess;

import com.fantasysport.models.*;
import com.fantasysport.models.fwc.FWCData;
import com.fantasysport.models.nonfantasy.NFData;
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

    private void raiseOnSignOut(){
        if(_listener == null){
            return;
        }
        _listener.onSignOut();
    }

    private void raiseOnMarketsLoaded(MarketsContainer container){
        if(_listener == null){
            return;
        }
        _listener.onMarkets(container);
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

    private  void raiseOnNFData(NFData data){
        if(_listener == null){
            return;
        }
        _listener.onNonFantasyData(data);
    }

    private void raiseONFWCData(FWCData data){
        if(_listener == null){
            return;
        }
        raiseONFWCData(data);
    }

    public void loadDefaultRosterData(DefaultRosterData data){
        raiseOnDefaultRosterData(data);
    }

    public void loadMarkets(MarketsContainer container){
        raiseOnMarketsLoaded(container);
    }

    public void loadUserData(UserData data){
        raiseOnUserData(data);
    }

    public void loadNFData(NFData data){
        raiseOnNFData(data);
    }

    public void loadFWCData(FWCData data){

    }

    public void signOut(){
        raiseOnSignOut();
    }

    public interface IListener{
        public void onAccessTokenDataChanged(AccessTokenData data);
        public void onSignOut();
        public void onMarkets(MarketsContainer container);
        public void onDefaultRosterData(DefaultRosterData data);
        public void onUserData(UserData data);
        public void onNonFantasyData(NFData data);
        public void onFWCData(FWCData data);
    }
}