package com.fantasysport.views;

/**
 * Created by bylynka on 2/18/14.
 */
public class MenuItem {

    private int _id;
    private String _title;

    public MenuItem(int id, String title){
        _id = id;
        _title = title;
    }

    public int getId(){
        return _id;
    }

    public String getTitle(){
        return _title;
    }

}
