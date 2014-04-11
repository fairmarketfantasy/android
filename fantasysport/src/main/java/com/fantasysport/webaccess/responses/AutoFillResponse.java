package com.fantasysport.webaccess.responses;

import com.fantasysport.models.Roster;

/**
 * Created by bylynka on 3/4/14.
 */
public class AutoFillResponse {

    private Roster _roster;

    public AutoFillResponse(Roster roster){
        _roster = roster;
    }

    public Roster getRoster(){
        return _roster;
    }
}
