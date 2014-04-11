package com.fantasysport.utility;

/**
 * Created by bylynka on 4/11/14.
 */
public class OutParameter {

    private Object _parameter;

    public void setParameter(Object parameter){
        _parameter = parameter;
    }

    public <T> T getParameter(){
        return (T)_parameter;
    }
}
