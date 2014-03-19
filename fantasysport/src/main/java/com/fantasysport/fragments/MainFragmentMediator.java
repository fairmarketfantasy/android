package com.fantasysport.fragments;

import com.fantasysport.models.Market;
import com.fantasysport.models.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bylynka on 3/18/14.
 */
public class MainFragmentMediator {

    private List<IMarketListener> _marketListeners = new ArrayList<IMarketListener>();
    private List<IRemainingSalaryListener> _remainingSalaryListeners = new ArrayList<IRemainingSalaryListener>();
    private List<IPlayerAddListener> _playerAddListeners = new ArrayList<IPlayerAddListener>();
    private static MainFragmentMediator _instance;

    private MainFragmentMediator(){}


    public static MainFragmentMediator instance(){
        if(_instance == null){
            synchronized (MainFragmentMediator.class){
                if(_instance  == null){
                    _instance = new MainFragmentMediator();
                }
            }
        }
        return _instance;
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
}
