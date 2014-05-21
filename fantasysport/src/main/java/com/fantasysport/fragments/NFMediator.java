package com.fantasysport.fragments;

import com.fantasysport.models.nonfantasy.NFTeam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 5/19/14.
 */
public class NFMediator {

    private List<ITeamSelectedListener> _teamSelectedListeners = new ArrayList<ITeamSelectedListener>();
    private List<ITeamRosterUpdatedListener> _teamRosterUpdatedListeners = new ArrayList<ITeamRosterUpdatedListener>();
    private List<ITeamRemovedListener> _teamRemovedListeners = new ArrayList<ITeamRemovedListener>();


    public void addTeamRemovedListener(ITeamRemovedListener listener){
        _teamRemovedListeners.add(listener);
    }

    public void addTeamRosterUpdatedListener(ITeamRosterUpdatedListener listener){
        _teamRosterUpdatedListeners.add(listener);
    }

    public void addTeamSelectedListener(ITeamSelectedListener listener){
        _teamSelectedListeners.add(listener);
    }

    public void selectTeam(Object sender, NFTeam team){
        for (ITeamSelectedListener listener : _teamSelectedListeners){
            listener.onSelectedTeam(sender, team);
        }
    }

    public void removeTeamFromRoster(Object sender, NFTeam team){
        for (ITeamRemovedListener listener : _teamRemovedListeners){
            listener.onRemovedTeam(sender, team);
        }
    }

    public void updateRoster(Object sender, List<NFTeam> teams){
        for (ITeamRosterUpdatedListener listener : _teamRosterUpdatedListeners){
            listener.onUpdateRoster(sender, teams);
        }
    }

    public interface ITeamSelectedListener {
        void onSelectedTeam(Object sender, NFTeam team);
    }

    public interface ITeamRosterUpdatedListener {
        void onUpdateRoster(Object sender, List<NFTeam> teams);
    }

    public interface ITeamRemovedListener {
        void onRemovedTeam(Object sender, NFTeam team);
    }
}
