package com.zhengsonglan.jiujiu.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhengsonglan.jiujiu.app.AppConfig;

import cn.bmob.v3.Bmob;

/**
 * BaseActivity
 * Created by zsl on 2014/12/1.
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initConfig();
    }
    //显示土司
    public void showToast(String toast){
        Toast.makeText(getApplicationContext(),toast,Toast.LENGTH_SHORT).show();
    }

    /**
     * 初始化配置文件
     */
    public void initConfig(){
        //百度配置文件
//       boolean isSuccess= Frontia.init(getApplicationContext(),AppConfig.baiduAPIKey);
//        showToast(isSuccess+"");
        //Bomb配置文件
        Bmob.initialize(getApplicationContext(), AppConfig.bmobAPPID);
        //初始化语音功能
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=" + AppConfig.FeiXunAppid);
        //配置imageloader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
    }
}
