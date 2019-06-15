package com.db.util;

public class TextUtil {
    public static boolean isEmpty(String str){
        if(str==null){
            return true;
        }else return str.equals("");
    }
}
