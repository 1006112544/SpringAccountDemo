package com.db.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
    public static String createMD5(String password,String salt) {
        String str = password + salt;
        StringBuffer stringBuffer = null;
        String hexString = null;
        try {
                //1.指定加密算法类型
                MessageDigest digest = MessageDigest.getInstance("MD5");
                //2.将需要加密的字符串转换成byte数组
                byte[] hash = digest.digest(str.getBytes("UTF-8"));
                //3.拼接数据
                stringBuffer = new StringBuffer();
                for (byte b : hash) {
                    int i = b&0xFF;
                    //int类型的i需要转换成16进制
                    hexString = Integer.toHexString(i);
                    if(hexString.length()<2) {
                         hexString = "0"+hexString;
                    }
                    stringBuffer.append(hexString);
                }
        } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        return stringBuffer.toString();
    }
}
