package com.fantasysport.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bylynka on 3/24/14.
 */
public class Avatar {

    @SerializedName("file")
    private String _file;

    @SerializedName("filename")
    private String _fileName;

    @SerializedName("original_filename")
    private String _originalFileName;

    public Avatar(String file, String fileName, String originalFileName){
        _file = file;
        _fileName = fileName;
        _originalFileName = originalFileName;
    }
}
