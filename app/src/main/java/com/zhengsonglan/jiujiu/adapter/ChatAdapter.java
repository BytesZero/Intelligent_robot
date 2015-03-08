package com.zhengsonglan.jiujiu.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhengsonglan.jiujiu.R;
import com.zhengsonglan.jiujiu.adapter.utils.ViewHolderUtil;
import com.zhengsonglan.jiujiu.entity.ChatMsgEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 聊天的adapter
 * Created by zsl on 2014/12/12.
 */
public class ChatAdapter extends BaseAdapter {
    Context context;
    List<ChatMsgEntity> data;
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    ImageLoader imageLoader;

    //聊天的item对象
    TextView tv_msg,tv_time,tv_url;
    LinearLayout ll_base;

    public ChatAdapter(Context context, List<ChatMsgEntity> data) {
        this.context = context;
        this.data = data;
        imageLoader=ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        return data==null?0:data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        //获取聊天数据
        ChatMsgEntity chatMsgEntity=data.get(position);
        if (chatMsgEntity.getType()== ChatMsgEntity.Type.you){
            String msg=chatMsgEntity.getMsg();
            String code=getMsgCode(msg);

            if ("100000".equals(code)){//文本
                convertView = baseMsg(parent, chatMsgEntity, msg);
            }else if ("200000".equals(code)){//链接
                convertView= LayoutInflater.from(context).inflate(R.layout.chat_item_left_200000,parent,false);
                tv_msg= ViewHolderUtil.get(convertView,R.id.chat_item_left200000_tv_msg);
                tv_time= ViewHolderUtil.get(convertView,R.id.chat_item_left200000_tv_time);
                tv_url=ViewHolderUtil.get(convertView,R.id.chat_item_left200000_tv_url);
                final String url=getMsgUrl(msg);
//                tv_url.setText(Html.fromHtml("<u>"+url+"</u>"));
                tv_url.setText(Html.fromHtml("<u>点击查看</u>"));
                tv_url.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        context.startActivity(intent);
                    }
                });

                //添加数据
                tv_msg.setText(getMsgText(msg));
                tv_time.setText(simpleDateFormat.format(chatMsgEntity.getDate()));
            }else if ("302000".equals(code)){//新闻
                convertView=base_msg(parent,chatMsgEntity);
                String lists=getLists(msg);
                JSONArray jsonArray= JSON.parseArray(lists);
                for (int i=0;i<jsonArray.size()%5;i++)
                {
                    JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                    View view=LayoutInflater.from(context).inflate(R.layout.chat_item_left_item_302000, null, false);
                    TextView item_tv_article=ViewHolderUtil.get(view,R.id.chat_item_left_item_302000_tv_article);
                    TextView item_tv_source=ViewHolderUtil.get(view,R.id.chat_item_left_item_302000_tv_source);
                    ImageView item_tv_icon=ViewHolderUtil.get(view,R.id.chat_item_left_item_302000_iv_icon);
                    item_tv_article.setText(jsonObject.getString("article"));
                    item_tv_source.setText("来自:"+jsonObject.getString("source"));
                    String icon=jsonObject.getString("icon");
                    if (icon!=null&&!"".equals(icon)){
                        imageLoader.displayImage(icon,item_tv_icon);
                    }
                    view.setTag(jsonObject.getString("detailurl"));
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(v.getTag().toString()));
                            context.startActivity(intent);
                        }
                    });
                    ll_base.addView(view);
                }


            }else if ("304000".equals(code)){//新闻
                convertView=base_msg(parent,chatMsgEntity);
                String lists=getLists(msg);
                JSONArray jsonArray= JSON.parseArray(lists);
                for (int i=0;i<5;i++)
                {
                    JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                    View view=LayoutInflater.from(context).inflate(R.layout.chat_item_left_item_304000, null, false);
                    TextView item_tv_title=ViewHolderUtil.get(view,R.id.chat_item_left_item_304000_tv_title);
                    TextView item_tv_info=ViewHolderUtil.get(view,R.id.chat_item_left_item_304000_tv_info);
                    ImageView item_tv_icon=ViewHolderUtil.get(view,R.id.chat_item_left_item_304000_iv_icon);
                    item_tv_title.setText(jsonObject.getString("name"));
                    item_tv_info.setText("下载量:"+jsonObject.getString("count"));
                    String icon=jsonObject.getString("icon");
                    if (icon!=null&&!"".equals(icon)){
                        imageLoader.displayImage(icon,item_tv_icon);
                    }
                    view.setTag(jsonObject.getString("detailurl"));
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(v.getTag().toString()));
                            context.startActivity(intent);
                        }
                    });
                    ll_base.addView(view);
                }


            }else if ("308000".equals(code)){//电影
                convertView=base_msg(parent,chatMsgEntity);
                String lists=getLists(msg);
                JSONArray jsonArray= JSON.parseArray(lists);
                for (int i=0;i<5;i++)
                {
                    JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                    View view=LayoutInflater.from(context).inflate(R.layout.chat_item_left_item_304000, null, false);
                    TextView item_tv_title=ViewHolderUtil.get(view,R.id.chat_item_left_item_304000_tv_title);
                    TextView item_tv_info=ViewHolderUtil.get(view,R.id.chat_item_left_item_304000_tv_info);
                    ImageView item_tv_icon=ViewHolderUtil.get(view,R.id.chat_item_left_item_304000_iv_icon);
                    item_tv_title.setText(jsonObject.getString("name"));
                    item_tv_info.setText("详情:"+jsonObject.getString("info"));
                    String icon=jsonObject.getString("icon");
                    if (icon!=null&&!"".equals(icon)){
                        imageLoader.displayImage(icon,item_tv_icon);
                    }
                    view.setTag(jsonObject.getString("detailurl"));
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(v.getTag().toString()));
                            context.startActivity(intent);
                        }
                    });
                    ll_base.addView(view);
                }


            }else if ("311000".equals(code)){//电影
                convertView=base_msg(parent,chatMsgEntity);
                String lists=getLists(msg);
                JSONArray jsonArray= JSON.parseArray(lists);
                for (int i=0;i<5;i++)
                {
                    JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                    View view=LayoutInflater.from(context).inflate(R.layout.chat_item_left_item_304000, null, false);
                    TextView item_tv_title=ViewHolderUtil.get(view,R.id.chat_item_left_item_304000_tv_title);
                    TextView item_tv_info=ViewHolderUtil.get(view,R.id.chat_item_left_item_304000_tv_info);
                    ImageView item_tv_icon=ViewHolderUtil.get(view,R.id.chat_item_left_item_304000_iv_icon);
                    item_tv_title.setText(jsonObject.getString("name"));
                    item_tv_info.setText("价格:"+jsonObject.getString("price"));
                    String icon=jsonObject.getString("icon");
                    if (icon!=null&&!"".equals(icon)){
                        imageLoader.displayImage(icon,item_tv_icon);
                    }
                    view.setTag(jsonObject.getString("detailurl"));
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(v.getTag().toString()));
                            context.startActivity(intent);
                        }
                    });
                    ll_base.addView(view);
                }


            }else if ("309000".equals(code)){//酒店
                convertView=base_msg(parent,chatMsgEntity);
                String lists=getLists(msg);
                JSONArray jsonArray= JSON.parseArray(lists);
                for (int i=0;i<5;i++)
                {
                    JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                    View view=LayoutInflater.from(context).inflate(R.layout.chat_item_left_item_309000, null, false);
                    TextView item_tv_name=ViewHolderUtil.get(view,R.id.chat_item_left_item_309000_tv_name);
                    TextView item_tv_satisfaction=ViewHolderUtil.get(view,R.id.chat_item_left_item_309000_tv_satisfaction);
                    TextView item_tv_price=ViewHolderUtil.get(view,R.id.chat_item_left_item_309000_tv_price);
                    ImageView item_tv_icon=ViewHolderUtil.get(view,R.id.chat_item_left_item_309000_iv_icon);
                    item_tv_name.setText(jsonObject.getString("name"));
                    item_tv_satisfaction.setText("评价:"+jsonObject.getString("satisfaction"));
                    item_tv_price.setText(jsonObject.getString("price"));
                    String icon=jsonObject.getString("icon");
                    if (icon!=null&&!"".equals(icon)){
                        imageLoader.displayImage(icon,item_tv_icon);
                    }
                    view.setTag(jsonObject.getString("detailurl"));
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String data=v.getTag().toString();
                            if (data!=null&&!data.equals("")){
                                Intent intent=new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(data));
                                context.startActivity(intent);
                            }else{
                                showToast();
                            }
                        }
                    });
                    ll_base.addView(view);
                }


            }else if ("305000".equals(code)){//火车
                convertView=base_msg(parent,chatMsgEntity);
                String lists=getLists(msg);
                JSONArray jsonArray= JSON.parseArray(lists);
                for (int i=0;i<5;i++)
                {
                    JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                    View view=LayoutInflater.from(context).inflate(R.layout.chat_item_left_item_305000, null, false);
                    TextView item_tv_trainnum=ViewHolderUtil.get(view,R.id.chat_item_left_item_305000_tv_trainnum);
                    TextView item_tv_start=ViewHolderUtil.get(view,R.id.chat_item_left_item_305000_tv_start);
                    TextView item_tv_terminal=ViewHolderUtil.get(view,R.id.chat_item_left_item_305000_tv_terminal);
                    TextView item_tv_starttime=ViewHolderUtil.get(view,R.id.chat_item_left_item_305000_tv_starttime);
                    TextView item_tv_endtime=ViewHolderUtil.get(view,R.id.chat_item_left_item_305000_tv_endtime);
                    item_tv_trainnum.setText(jsonObject.getString("trainnum"));
                    item_tv_start.setText(jsonObject.getString("start"));
                    item_tv_terminal.setText(jsonObject.getString("terminal"));
                    item_tv_starttime.setText(jsonObject.getString("starttime"));
                    item_tv_endtime.setText(jsonObject.getString("endtime"));
                    view.setTag(jsonObject.getString("detailurl"));
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(v.getTag().toString()));
                            context.startActivity(intent);
                        }
                    });
                    ll_base.addView(view);
                }
            }else{
                convertView = baseMsg(parent, chatMsgEntity, msg);
            }

        }else{
            convertView= LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false);
            tv_msg= ViewHolderUtil.get(convertView,R.id.chat_item_right_tv_msg);
            tv_time= ViewHolderUtil.get(convertView,R.id.chat_item_right_tv_time);
            String msg=chatMsgEntity.getMsg();
            //添加数据
            tv_msg.setText(msg);
            tv_time.setText(simpleDateFormat.format(chatMsgEntity.getDate()));
        }
        return convertView;
    }

    private void showToast() {
        Toast.makeText(context, "没有详情了", Toast.LENGTH_SHORT).show();
    }

    private View baseMsg(ViewGroup parent, ChatMsgEntity chatMsgEntity, String msg) {
        View convertView;
        convertView= LayoutInflater.from(context).inflate(R.layout.chat_item_left_100000,parent,false);
        tv_msg= ViewHolderUtil.get(convertView, R.id.chat_item_left100000_tv_msg);
        tv_time= ViewHolderUtil.get(convertView,R.id.chat_item_left100000_tv_time);
        //添加数据
        tv_msg.setText(getMsgText(msg));
        tv_time.setText(simpleDateFormat.format(chatMsgEntity.getDate()));
        return convertView;
    }

    /**
     * 重构基础消息
     */
    private View base_msg(ViewGroup parent,ChatMsgEntity chatMsgEntity){
        View convertView= LayoutInflater.from(context).inflate(R.layout.chat_item_left_base,parent,false);
        ll_base=ViewHolderUtil.get(convertView,R.id.chat_item_left_base_ll_content);
        tv_time=ViewHolderUtil.get(convertView,R.id.chat_item_left_base_tv_time);
        tv_msg=ViewHolderUtil.get(convertView,R.id.chat_item_left_base_tv_msg);
        //添加数据
        tv_msg.setText(getMsgText(chatMsgEntity.getMsg()));
        tv_time.setText(simpleDateFormat.format(chatMsgEntity.getDate()));
        return  convertView;
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
     * 获得Text
     * @param msg
     * @return
     */
    private String getMsgText(String msg) {
        try {
            JSONObject jsonObject= JSONObject.parseObject(msg);
            String text=jsonObject.getString("text");
            return text;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获得Lists
     * @param msg
     * @return
     */
    private String getLists(String msg) {
        try {
            JSONObject jsonObject= JSONObject.parseObject(msg);
            String text=jsonObject.getString("list");
            return text;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获得List
     * @param msg
     * @return
     */
    private List<Map<String,String>> getListsContent(String msg,String [] msgType) {

        List<Map<String,String>> mapList=new ArrayList<Map<String,String>>();
        try {
            JSONArray jsonArray= JSON.parseArray(msg);
            for (int i=0;i<3;i++){
                JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                Map<String,String> stringMap=new HashMap<String,String>();
                for (int j=0;j<msgType.length;j++){
                    String msgTypeString=msgType[i];
                    stringMap.put(msgTypeString,jsonObject.getString(msgTypeString));
                }
                mapList.add(stringMap);
            }

            return mapList;
        } catch (Exception e) {
            return mapList;
        }
    }


}
