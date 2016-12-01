package com.janabo.ryun.http;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.fragment.ConversationFragment;

/**
 * 作者：janabo on 2016/11/30 11:20
 */
public class MySendConversationFragment extends ConversationFragment {
    Context mContext = this.getActivity().getApplicationContext();
    String fromid;
    String toid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    public MySendConversationFragment() {
    }

    @Override
    public void onSendToggleClick(View v, String text) {
        super.onSendToggleClick(v, text);
 //       sendToServer(text);
    }

    /**
     * 点击 “+” 号区域, 回调中携带 ViewGroup
     *
     * @param v              “+” 号 view 实例
     * @param extensionBoard 用于展示 plugin 的 ViewGroup
     */
    @Override
    public void onPluginToggleClick(View v, ViewGroup extensionBoard) {
        super.onPluginToggleClick(v, extensionBoard);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        super.beforeTextChanged(s, start, count, after);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        super.onTextChanged(s, start, before, count);
    }

    public void sendToServer(String text){
        Map<String,String> map = new HashMap<>();
        map.put("fromid",fromid);
        map.put("toid",toid);
        map.put("content",text);
        HttpClientUtil.doPost("http://192.168.3.68:8192/hprongyun.asmx/SendMessage", map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(mContext,"发送成功",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(mContext,"发送失败",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
        });
    }

    public void setFromid(String fromid) {
        this.fromid = fromid;
    }

    public void setToid(String toid) {
        this.toid = toid;
    }
}
