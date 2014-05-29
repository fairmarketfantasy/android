package com.fantasysport.fragments.pages.nonfantasy;

import com.fantasysport.fragments.pages.IMediator;
import com.fantasysport.models.nonfantasy.NFAutoFillData;
import com.fantasysport.models.nonfantasy.NFGame;
import com.fantasysport.models.nonfantasy.NFTeam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 5/19/14.
 */
public class NFMediator implements IMediator {

    private List<ITeamSelectedListener> _teamSelectedListeners = new ArrayList<ITeamSelectedListener>();
    private List<ITeamRosterUpdatedListener> _teamRosterUpdatedListeners = new ArrayList<ITeamRosterUpdatedListener>();
    private List<ITeamRemovedListener> _teamRemovedListeners = new ArrayList<ITeamRemovedListener>();
    private List<IUpdateGamesRequestListener> _updateGamesRequestListeners = new ArrayList<IUpdateGamesRequestListener>();
    private List<IGamesUpdatedListener> _gamesUpdatedListeners = new ArrayList<IGamesUpdatedListener>();
    private List<IAutoFillRequestListener> _autoFillRequestListeners = new ArrayList<IAutoFillRequestListener>();
    private List<IAutoFillDataListener> _autoFillDataListeners = new ArrayList<IAutoFillDataListener>();
    private List<IOnDataUpdatedListener> _onDataUpdatedListeners = new ArrayList<IOnDataUpdatedListener>();

    public void addOnDataUpdatedListener(IOnDataUpdatedListener listener){
        _onDataUpdatedListeners.add(listener);
    }

    public void addAutoFillDataListener(IAutoFillDataListener listener){
        _autoFillDataListeners.add(listener);
    }

    public void addAutoFillRequestListener(IAutoFillRequestListener listener){
        _autoFillRequestListeners.add(listener);
    }

    public void addGamesUpdatedListener(IGamesUpdatedListener listener){
        _gamesUpdatedListeners.add(listener);
    }

    public void addUpdateGamesRequestListener(IUpdateGamesRequestListener listener){
        _updateGamesRequestListeners.add(listener);
    }

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

    public void updateData(Object sender){
        for(IOnDataUpdatedListener listener : _onDataUpdatedListeners){
            listener.onDataUpdated(sender);
        }
    }

    public void updateRoster(Object sender, List<NFTeam> teams){
        for (ITeamRosterUpdatedListener listener : _teamRosterUpdatedListeners){
            listener.onUpdateRoster(sender, teams);
        }
    }

    public void requestUpdateGames(Object sender){
        for(IUpdateGamesRequestListener listener : _updateGamesRequestListeners){
            listener.onUpdatesGameRequest(sender);
        }
    }

    public void requestAutoFill(Object sender){
        for (IAutoFillRequestListener listener : _autoFillRequestListeners){
            listener.onRequestAutoFill(sender);
        }
    }

    public void gamesUpdated(Object sender, List<NFGame> games){
        for(IGamesUpdatedListener listener : _gamesUpdatedListeners){
            listener.onGamesUpdated(sender, games);
        }
    }

    public void setNFAutoFillData(Object sender, NFAutoFillData data){
        for (IAutoFillDataListener listener : _autoFillDataListeners){
            listener.onAutoFillData(sender, data);
        }
    }

/**********************************************************************************************************************/
    public interface ITeamSelectedListener {
        void onSelectedTeam(Object sender, NFTeam team);
    }

    public interface ITeamRosterUpdatedListener {
        void onUpdateRoster(Object sender, List<NFTeam> teams);
    }

    public interface ITeamRemovedListener {
        void onRemovedTeam(Object sender, NFTeam team);
    }

    public interface IUpdateGamesRequestListener{
        void onUpdatesGameRequest(Object sender);
    }

    public interface IGamesUpdatedListener{
        void onGamesUpdated(Object sender, List<NFGame> games);
    }

    public interface IAutoFillRequestListener{
        void onRequestAutoFill(Object sender);
    }

    public interface IAutoFillDataListener{
        void onAutoFillData(Object sender, NFAutoFillData data);
    }

    public interface IOnDataUpdatedListener {
        void onDataUpdated(Object sender);
    }
}
