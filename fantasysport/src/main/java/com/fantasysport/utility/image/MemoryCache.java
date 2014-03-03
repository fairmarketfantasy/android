package com.fantasysport.utility.image;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * Created by bylynka on 3/1/14.
 */

public class MemoryCache {

    private static final String TAG = "MemoryCache";

    private Map<String, Bitmap> _cache = Collections.synchronizedMap(
            new LinkedHashMap<String, Bitmap>(10,1.5f,true));

    private long _size =0;

    //max memory _cache folder used to download images in bytes
    private long _limit = 1000000;

    public MemoryCache(){
        //use 25% of available heap _size
        setLimit(Runtime.getRuntime().maxMemory() / 4);
    }

    public void setLimit(long newLimit){

        _limit = newLimit;
        Log.i(TAG, "MemoryCache will use up to "+ _limit /1024./1024.+"MB");
    }

    public Bitmap get(String id){
        try{
            if(!_cache.containsKey(id))
                return null;

            return _cache.get(id);

        }catch(NullPointerException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public void put(String id, Bitmap bitmap){
        try{
            if(_cache.containsKey(id))
                _size -=getSizeInBytes(_cache.get(id));
            _cache.put(id, bitmap);
            _size +=getSizeInBytes(bitmap);
            checkSize();
        }catch(Throwable th){
            th.printStackTrace();
        }
    }

    private void checkSize() {
        Log.i(TAG, "_cache _size=" + _size + " length=" + _cache.size());
        if(_size > _limit){

            //least recently accessed item will be the first one iterated
            Iterator<Entry<String, Bitmap>> iter= _cache.entrySet().iterator();

            while(iter.hasNext()){
                Entry<String, Bitmap> entry=iter.next();
                _size -=getSizeInBytes(entry.getValue());
                iter.remove();
                if(_size <= _limit)
                    break;
            }
            Log.i(TAG, "Clean _cache. New _size "+ _cache.size());
        }
    }

    public void clear() {
        try{
            // Clear _cache
            _cache.clear();
            _size =0;
        }catch(NullPointerException ex){
            ex.printStackTrace();
        }
    }

    long getSizeInBytes(Bitmap bitmap) {
        if(bitmap==null)
            return 0;
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}