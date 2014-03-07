package com.fantasysport.utility;

/**
 * Created by bylynka on 3/6/14.
 */
public class StringHelper {

    public static String capitalizeFirstLetter(String str){
        if(str == null || str.length() == 0){
            return str;
        }
        if(str.length() == 1){
            return str.toUpperCase();
        }
       return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
