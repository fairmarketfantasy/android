package com.fantasysport.utility.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import com.fantasysport.utility.Size;

import java.io.ByteArrayOutputStream;

/**
 * Created by bylynka on 3/24/14.
 */
public class BitmapUtils {

    public static String toBase64(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap resize(Bitmap bitmap,  Size reqSize){
        Size size = calculateInSampleSize(new Size(bitmap.getWidth(), bitmap.getHeight()), reqSize);
        return Bitmap.createScaledBitmap(bitmap, 120, 120, false);
    }

    public static Size calculateInSampleSize(Size origSize, Size reqSize) {
        int scaleFactor = 1;
        int origSide;
        int reqSide;
        if(origSize.getHeight() > origSize.getWidth()){
            origSide = origSize.getHeight();
            reqSide = reqSize.getHeight();
        }else{
            origSide = origSize.getWidth();
            reqSide = reqSize.getWidth();
        }
        scaleFactor = origSide < reqSide? scaleFactor: Math.round(origSide/reqSide);
        return new Size(origSize.getWidth()*scaleFactor, origSize.getHeight()*scaleFactor);
    }

}
