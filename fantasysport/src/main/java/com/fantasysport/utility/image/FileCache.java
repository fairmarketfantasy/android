package com.fantasysport.utility.image;

import java.io.File;
import android.content.Context;
/**
 * Created by bylynka on 3/1/14.
 */
public class FileCache {

    private File _cacheDir;

    public FileCache(Context context){
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)){
            _cacheDir = new File(
                    android.os.Environment.getExternalStorageDirectory(),"LazyList");
        }
        else{
            _cacheDir =context.getCacheDir();
        }

        if(!_cacheDir.exists()){
            _cacheDir.mkdirs();
        }
    }

    public File getFile(String url){
        String filename=String.valueOf(url.hashCode());

        File f = new File(_cacheDir, filename);
        return f;

    }

    public void clear(){
        File[] files= _cacheDir.listFiles();
        if(files==null){
            return;
        }

        for(File f:files){
            f.delete();
        }
    }

}