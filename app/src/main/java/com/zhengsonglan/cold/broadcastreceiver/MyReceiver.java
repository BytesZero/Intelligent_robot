package com.zhengsonglan.cold.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.alibaba.fastjson.JSONObject;
import com.zhengsonglan.cold.entity.ChatMsgEntity;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action=intent.getAction();
        if ("url".equals(action)) {
            ChatMsgEntity chatMsgEntity= (ChatMsgEntity)intent.getExtras().get("chatMsgEntity");
            String msg=chatMsgEntity.getMsg();
            if ("200000".equals(getMsgCode(msg))){
                String url=getMsgUrl(msg);
                Intent intent1=new Intent(Intent.ACTION_VIEW);
                intent1.setData(Uri.parse(url));
                context.startActivity(intent1);
            }

        }
    }

    /**
     * 获得url
     * @param msg
     * @return
     */
    private String getMsgUrl(String msg) {
        try {
            JSONObject jsonObject= JSONObject.parseObject(msg);
            String url=jsonObject.getString("url");
            return url;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获得消息根据消息的类型
     * @param msg
     * @return
     */
    private String getMsgCode(String msg) {
        try {
            JSONObject jsonObject= JSONObject.parseObject(msg);
            String type=jsonObject.getString("code");
            return type;
        } catch (Exception e) {
            return "100000";
        }
    }
}
