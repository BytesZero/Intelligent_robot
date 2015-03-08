package com.zhengsonglan.jiujiu.activitys.main;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.zhengsonglan.jiujiu.R;
import com.zhengsonglan.jiujiu.activitys.BaseActivity;
import com.zhengsonglan.jiujiu.adapter.ChatAdapter;
import com.zhengsonglan.jiujiu.app.AppConfig;
import com.zhengsonglan.jiujiu.broadcastreceiver.MyReceiver;
import com.zhengsonglan.jiujiu.entity.ChatMsgEntity;
import com.zhengsonglan.jiujiu.utils.JsonParser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * login page
 * Created by zsl on 2014/12/1.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    //initview
    TextView tv_sound_insert,tv_send;
    EditText et_input_msg;
    ListView lv_chat;

    //飞讯科大的语音听写对象
    SpeechRecognizer recognizer;
    //语音听写对话框
    RecognizerDialog dialog;
    //initdata
    List<ChatMsgEntity> data;
    ChatAdapter chatAdapter;
    String you_name="啾啾";
    String me_name="me";
    MyReceiver myReceiver=new MyReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        initData();
    }

    //启动就加载录音
    @Override
    protected void onResume() {
        super.onResume();
//        tv_sound_insert.performClick();
    }


    //初始化控件
    private void initView(){
        tv_sound_insert=(TextView)findViewById(R.id.main_tv_sound_insert);
        lv_chat= (ListView) findViewById(R.id.main_lv_chat);
        tv_send= (TextView) findViewById(R.id.main_tv_send);
        et_input_msg= (EditText) findViewById(R.id.main_et_input_msg);
    }
    //初始化数据
    private void initData(){
        //注册广播
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.zhengsonglan.cold.broadcastreceiver.MyReceiver");
        registerReceiver(myReceiver, intentFilter);

        //初始化语音听写
        recognizer=SpeechRecognizer.createRecognizer(this,initListener);
        //初始化UI
        dialog=new RecognizerDialog(this,initListener);
        //初始化聊天数据
        data=new ArrayList<ChatMsgEntity>();
        ChatMsgEntity chatMsgEntity=new ChatMsgEntity(you_name,"{\"code\":100000,\"text\":\"你好，啾啾为你服务\"}", ChatMsgEntity.Type.you,new Date());
        data.add(chatMsgEntity);
        chatAdapter=new ChatAdapter(this,data);
        lv_chat.setAdapter(chatAdapter);


    }
    //初始化事件
    private void initEvent(){
        //弹出录音框
        tv_sound_insert.setOnClickListener(this);
        tv_send.setOnClickListener(this);
    }

    /**
     * Handler
     */
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            ChatMsgEntity chatMsgEntity= (ChatMsgEntity) msg.obj;
            data.add(chatMsgEntity);
            chatAdapter.notifyDataSetChanged();
            lv_chat.setSelection(data.size()-1);

            //发送打开广播的测试
            Intent intent=new Intent();
            intent.setAction("url");
            intent.putExtra("chatMsgEntity",chatMsgEntity);
            sendBroadcast(intent);
        }
    };

    /**
     *语音输入对话框
     */
    private void showFeiXunDialog() {
        setParams();
        dialog.setListener(recognizerDialogListener);
        dialog.show();
    }

    /**
     * 发送消息的方法
     */
    private void sendMsg(String msg){
        ChatMsgEntity chatMsgEntity=new ChatMsgEntity(me_name,msg, ChatMsgEntity.Type.me,new Date());
        Message message=handler.obtainMessage();
        message.obj=chatMsgEntity;
        handler.sendMessage(message);
        et_input_msg.setText("");
        replyMsg(msg);

    }

    /**
     * 接收消息
     * @param msg
     */
    private void replyMsg(final String msg){
        try {
            String INFO = URLEncoder.encode(msg, "utf-8");
            String url = "http://www.tuling123.com/openapi/api?key="+ AppConfig.TULINGAPIKEY+"&info="+INFO;
            RequestQueue queue= Volley.newRequestQueue(this);
            queue.add(new StringRequest(url,new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    replyMsgHandler(s);
                }
            },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    replyMsgHandler("{\"code\":100000,\"text\":\"您的问题太深奥了，啾啾难以回答1\"}");
                }
            }));
        } catch (UnsupportedEncodingException e) {
            replyMsgHandler("{\"code\":100000,\"text\":\"您的问题太深奥了，啾啾难以回答2\"}");
        }

    }

    /**
     * 接收到消息发送到handler
     * @param msg
     */
    private void replyMsgHandler(String msg) {
        ChatMsgEntity chatMsgEntity=new ChatMsgEntity(you_name,msg, ChatMsgEntity.Type.you,new Date());
        Message message=handler.obtainMessage();
        message.obj=chatMsgEntity;
        handler.sendMessage(message);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_tv_sound_insert:
            {
                showFeiXunDialog();
                break;
            }
            case R.id.main_tv_send:
            {
                String msg=et_input_msg.getText().toString();
                if (msg!=null&&msg.equals("")){
                    showToast("发送消息不能为空哦");
                    return;
                }
                sendMsg(msg);
                break;
            }

        }
    }

    @Override
    protected void onDestroy() {
        //取消销毁语音听写
        recognizer.cancel();
        recognizer.destroy();
        super.onDestroy();
        if(myReceiver!=null){
            this.unregisterReceiver(myReceiver);
        }
    }

    /**
     * 设置语音参数
     */
    private void setParams(){
        // 清空参数
        recognizer.setParameter(SpeechConstant.PARAMS, null);
        // 设置语言
        recognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        recognizer.setParameter(SpeechConstant.ACCENT,"mandarin");
        // 设置语音前端点
        recognizer.setParameter(SpeechConstant.VAD_BOS,"4000");
        // 设置语音后端点
        recognizer.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置标点符号
        recognizer.setParameter(SpeechConstant.ASR_PTT, "1");
        // 设置音频保存路径
        recognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/iflytek/wavaudio.pcm");
    }
    /**
     * 初始化的的监听
     */
    private InitListener initListener=new InitListener() {
        @Override
        public void onInit(int i) {

        }
    };
    /**
     * 对话框的监听
     */
    private RecognizerDialogListener recognizerDialogListener=new RecognizerDialogListener() {
        @Override
        public void onResult(RecognizerResult results, boolean b) {
            String text = JsonParser.parseIatResult(results.getResultString());
            et_input_msg.append(text);
        }

        @Override
        public void onError(SpeechError speechError) {
            showToast(speechError.getPlainDescription(true));
        }
    };

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

}
