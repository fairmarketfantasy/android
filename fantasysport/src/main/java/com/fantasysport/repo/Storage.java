package com.fantasysport.repo;

import com.fantasysport.models.UserData;

/**
 * Created by bylynka on 2/14/14.
 */
public class Storage {

    private static Storage _instance;

    private Storage() {}

    private UserData _userData;

    public static Storage instance() {
        if (_instance == null) {
            synchronized (Storage.class) {
                if (_instance == null) {
                    _instance = new Storage();
                }
            }
        }
        return _instance;
    }

    public void setUserData(UserData userData){
        _userData = userData;
    }

    public UserData getUserData(){
        return _userData;
    }


}
