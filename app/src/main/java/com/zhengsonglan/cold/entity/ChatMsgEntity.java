package com.zhengsonglan.cold.entity;

import android.view.Menu;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * 连天消息的实体类
 * Created by zsl on 2014/12/12.
 */
public class ChatMsgEntity implements Serializable {

    private String name,msg;
    private Type type;
    private Date date;
    public enum Type{
        you,me
    }

    public ChatMsgEntity(String name, String msg, Type type, Date date) {
        this.name = name;
        this.msg = msg;
        this.type = type;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
