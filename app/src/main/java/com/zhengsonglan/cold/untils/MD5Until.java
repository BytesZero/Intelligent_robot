package com.zhengsonglan.cold.untils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具类
 * Created by zsl on 2014/12/5.
 */
public class MD5Until {
    /**
     * 将字符串加密为32为的字符
     * @param plainText
     * @return
     */
    public static String getMd5(String plainText ) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if(i<0) i+= 256;
                if(i<16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            return buf.toString();


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return  "";
    }
}
