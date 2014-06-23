package com.fantasysport.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bylynka on 2/20/14.
 */
public class Converter {

    public static Date toDate(String string){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            return format.parse(string);
        }catch (Exception e){
            try{
                format = new SimpleDateFormat("yyyy-MM-dd");
                return format.parse(string);
            }
            catch (Exception ex){
                return new Date();
            }
        }
    }

    public static String toString(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return format.format(date);
    }
}
