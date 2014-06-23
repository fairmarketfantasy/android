package com.fantasysport.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by bylynka on 3/24/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Avatar {

    @JsonProperty("file")
    private String _file;

    @JsonProperty("filename")
    private String _fileName;

    @JsonProperty("original_filename")
    private String _originalFileName;

    public Avatar(String file, String fileName, String originalFileName){
        _file = file;
        _fileName = fileName;
        _originalFileName = originalFileName;
    }
}
