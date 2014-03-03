package com.fantasysport.parsers;

import java.util.HashMap;

/**
 * Created by bylynka on 2/26/14.
 */
public class BaseParser {

    protected int _fieldsNumber;
    protected HashMap<String, Integer> _keyMap;

    protected boolean atemptPutKey(String fieldName, String comparedField, int index){
        if(fieldName.equalsIgnoreCase(comparedField)){
            _keyMap.put(fieldName, index);
            return true;
        }
        return false;
    }

    protected Double getDouble(Object value){
        if(value == null){
            return  0d;
        }
        if(value instanceof Double){
            return (Double)value;
        }
        return Double.parseDouble((String)value);
    }
}
