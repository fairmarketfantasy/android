package com.fantasysport.parsers;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bylynka on 3/19/14.
 */
public class BsonDataWrapper {

    @SerializedName("data")
    private List<Object> _dataList;

    public List<Object> getDataList(){
        return _dataList;
    }
}
