package com.fantasysport.adapters;

/**
 * Created by bylynka on 2/18/14.
 */
public class MenuItem {

    private MenuItemEnum _id;
    private String _title;

    public MenuItem(MenuItemEnum id, String title){
        _id = id;
        _title = title;
    }

    public MenuItemEnum getId(){
        return _id;
    }

    public String getTitle(){
        return _title;
    }

}
