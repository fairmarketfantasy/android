package com.fantasysport.adapters;

import java.util.List;

/**
 * Created by bylynka on 2/18/14.
 */
public class MenuItem{

    private MenuItemEnum _id;
    private String _title;
    private List<MenuItem> _children;


    public MenuItem(MenuItemEnum id, String title){
        _id = id;
        _title = title;
    }

    public void setChildren(List<MenuItem> children){
        _children = children;
    }

    public List<MenuItem> getChildren(){
        return _children;
    }

    public MenuItemEnum getId(){
        return _id;
    }

    public String getTitle(){
        return _title;
    }

    public boolean hasChildren() {
        return _children != null;
    }
}
