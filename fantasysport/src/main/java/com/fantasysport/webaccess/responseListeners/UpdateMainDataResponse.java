package com.fantasysport.webaccess.responseListeners;

import com.fantasysport.models.Roster;
import com.fantasysport.models.UserData;

/**
 * Created by bylynka on 3/26/14.
 */
public class UpdateMainDataResponse {

    private UserData _userData;
    private Roster _roster;

    public UserData getUserData() {
        return _userData;
    }

    public void setUserData(UserData userData) {
        _userData = userData;
    }

    public Roster getRoster() {
        return _roster;
    }

    public void setRoster(Roster roster) {
        _roster = roster;
    }
}
