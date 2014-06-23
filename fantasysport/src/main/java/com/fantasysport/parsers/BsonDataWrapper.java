package com.fantasysport.parsers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by bylynka on 3/19/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BsonDataWrapper {

    @JsonProperty("data")
    private List<Object> _dataList;

    public List<Object> getDataList(){
        return _dataList;
    }
}
