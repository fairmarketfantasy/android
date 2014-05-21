package com.fantasysport.models.nonfantasy;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by bylynka on 5/21/14.
 */
public class NFGame implements Serializable {

    private NFTeam _homeTeam;
    private NFTeam _awayTeam;
    private Date _date;
    private int _statsId;

    public NFGame(){
    }

    public NFGame(NFTeam home, NFTeam away, Date date, int statsId){
        _homeTeam = home;
        _awayTeam = away;
        _date = date;
        _statsId = statsId;
    }

    public int getStatsId(){
        return _statsId;
    }

    public NFTeam getHomeTeam(){
        return _homeTeam;
    }

    public NFTeam getAwayTeam(){
        return _awayTeam;
    }

    public Date getDate(){
        return _date;
    }

    public void setDate(Date date){
        _date = date;
    }

}
