package com.fantasysport.fragments;

import com.fantasysport.adapters.nonfantasy.NFGameWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 5/19/14.
 */
public class NFMediator {

    private List<IGameSelectedListener> _gameSelectedListeners = new ArrayList<IGameSelectedListener>();
    private List<IGameRosterUpdatedListener> _gameRosterUpdatedListeners = new ArrayList<IGameRosterUpdatedListener>();


    public void addGameRosterUpdatedListener(IGameRosterUpdatedListener listener){
        _gameRosterUpdatedListeners.add(listener);
    }

    public void addGameSelectedListener(IGameSelectedListener listener){
        _gameSelectedListeners.add(listener);
    }

    public void selectGame(Object sender, NFGameWrapper gameWrapper){
        for (IGameSelectedListener listener : _gameSelectedListeners){
            listener.onSelectedGame(sender, gameWrapper);
        }
    }

    public void updateRoster(Object sender, List<NFGameWrapper> gameWrapper){
        for (IGameRosterUpdatedListener listener : _gameRosterUpdatedListeners){
            listener.onUpdateRoster(sender, gameWrapper);
        }
    }

    public interface IGameSelectedListener{
        void onSelectedGame(Object sender, NFGameWrapper gameWrapper);
    }

    public interface IGameRosterUpdatedListener{
        void onUpdateRoster(Object sender, List<NFGameWrapper> gameWrapper);
    }
}
