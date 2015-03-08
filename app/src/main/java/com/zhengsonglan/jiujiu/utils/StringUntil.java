package com.zhengsonglan.jiujiu.utils;

/**
 * 字符串的工具类
 * Created by zsl on 2014/12/5.
 */
public class StringUntil {
    /**
     * 判断字符串是否为空
     * @param content
     * @return
     */
    public static boolean isNull(String content){
        if (content!=null&&!"".equals(content)){
            return  false;
        }
        return  true;
    }

    /**
     * 判断两个字符串的内容是否相等
     * @param context
     * @param content2
     * @return
     */
    public static boolean isNotEquals(String context,String content2){
        if (context.equals(content2)){
            return false;
        }
        return  true;
    }
}
