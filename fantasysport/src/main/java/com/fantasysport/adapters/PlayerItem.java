package com.fantasysport.adapters;

/**
 * Created by bylynka on 2/25/14.
 */
public class PlayerItem {

    private String _position;
    private boolean _isSelected;
    private String _imageUrl;
    private String _name;

    public PlayerItem(String position){
        _isSelected = false;
        _position = position;
    }

    public String getPosition(){
        return _position;
    }

    public boolean isSelected(){
        return _isSelected;
    }

}
