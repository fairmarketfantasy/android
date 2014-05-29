package com.fantasysport.fragments.pages.fantasy;

import com.fantasysport.fragments.pages.IMediator;
import com.fantasysport.models.Market;
import com.fantasysport.models.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 3/18/14.
 */
public class MainFragmentMediator implements IMediator{

    private List<IMarketListener> _marketListeners = new ArrayList<IMarketListener>();
    private List<IRemainingSalaryListener> _remainingSalaryListeners = new ArrayList<IRemainingSalaryListener>();
    private List<IPlayerAddListener> _playerAddListeners = new ArrayList<IPlayerAddListener>();
    private List<IPlayerPositionListener> _playerPositionListeners = new ArrayList<IPlayerPositionListener>();
    private List<IOnBenchedStateChangedListener> _onBenchedStateChangedListeners = new ArrayList<IOnBenchedStateChangedListener>();
    private static MainFragmentMediator _instance;

    public MainFragmentMediator(){}

    public void addOnBenchedStateChangedListener(IOnBenchedStateChangedListener listener){
        _onBenchedStateChangedListeners.add(listener);
    }

    public void addPlayerPositionListener(IPlayerPositionListener listener){
        _playerPositionListeners.add(listener);
    }

    public void addPlayerAdListener(IPlayerAddListener listener){
        _playerAddListeners.add(listener);
    }

    public void addMarketListener(IMarketListener listener){
        _marketListeners.add(listener);
    }

    public void addRemainingSalaryListener(IRemainingSalaryListener listener){
        _remainingSalaryListeners.add(listener);
    }

    public void changeMarket(Object sender, Market market){
        raiseOnMarketChanged(sender, market);
    }

    public void changeRemainingSalary(Object sender, double remainingSalary){
        raiseOnRemainingSalaryChanged(sender, remainingSalary);
    }

    public void addPlayer(Object sender, Player player){
        raiseOnPlayerAdded(sender, player);
    }

    public void changePlayerPosition(Object sender, String posAcr){
        raiseOnPlayerPosition(sender, posAcr);
    }

    public void changeBenchedState(Object sender, boolean state){
        raiseOnBenchedStateChanged(sender, state);
    }

    private void raiseOnBenchedStateChanged(Object sender, boolean state){
        for (IOnBenchedStateChangedListener listener : _onBenchedStateChangedListeners){
            listener.onBenchedStateChanged(sender, state);
        }
    }

    private void raiseOnPlayerPosition(Object sender, String posAbr){
        for (IPlayerPositionListener listener: _playerPositionListeners){
            listener.onPlayerPositionChanged(sender, posAbr);
        }
    }

    private void raiseOnPlayerAdded(Object sender, Player player){
        for (int i = 0; i < _playerAddListeners.size(); i++){
            _playerAddListeners.get(i).onPlayerAdded(sender, player);
        }
    }

    private void raiseOnRemainingSalaryChanged(Object sender, double remainingSalary){
        for (int i = 0; i < _remainingSalaryListeners.size(); i++){
            _remainingSalaryListeners.get(i).onRemainingSalaryChanged(sender, remainingSalary);
        }
    }

    private void raiseOnMarketChanged(Object sender, Market market){
        for (int i = 0; i< _marketListeners.size(); i++){
            _marketListeners.get(i).onMarketChanged(sender, market);
        }
    }

    public interface IMarketListener {
        public void onMarketChanged(Object sender, Market market);
    }

    public interface IRemainingSalaryListener{
        public void onRemainingSalaryChanged(Object sender, double remainingSalary);
    }

    public interface IPlayerAddListener{
        public void onPlayerAdded(Object sender, Player player);
    }

    public interface IPlayerPositionListener{
        public void onPlayerPositionChanged(Object sender, String posAcr);
    }

    public interface IOnBenchedStateChangedListener {
        public void onBenchedStateChanged(Object sender, boolean state);
    }
}
