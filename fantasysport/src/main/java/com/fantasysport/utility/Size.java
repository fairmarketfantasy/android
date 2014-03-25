package com.fantasysport.utility;

/**
 * Created by bylynka on 3/24/14.
 */
public class Size {

    private int _width;
    private int _height;

    public Size(){
    }

    public Size(int width, int height){
        _width = width;
        _height = height;
    }

    public void setWidth(int width){
        _width = width;
    }

    public int getWidth(){
        return _width;
    }

    public void setHeight(int height){
        _height = height;
    }

    public int getHeight(){
        return _height;
    }

}
