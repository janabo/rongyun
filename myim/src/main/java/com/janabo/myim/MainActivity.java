package com.janabo.myim;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.janabo.myim.entity.Msg;
import com.janabo.myim.http.HttpClientUtil;
import com.janabo.myim.http.Manager;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    Context mContext = this;
    @ViewInject(R.id.msg_list_view)
    ListView msgListView;
    @ViewInject(R.id.input_text)
    EditText inputText;

    private MsgAdapter adapter;

    private List<Msg> msgList = new ArrayList<Msg>();
    CoreSharedPreferencesHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        helper = new CoreSharedPreferencesHelper(this);
        msgPoll();
        adapter = new MsgAdapter(MainActivity.this, R.layout.activity_msg_item, msgList);
        msgListView.setAdapter(adapter);
    }

    public void msgPoll(){
        handler.postDelayed(runnable, 2000);//每两秒执行一次runnable
    }

    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            initMsgs();
            handler.postDelayed(this, 2000);
        }
    };



    @Event(value={R.id.send,R.id.back})
    private void getEvent(View view){
        switch(view.getId()){
            case R.id.send:
                String content = inputText.getText().toString();
                if (content != null && !"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyDataSetChanged(); // 当有新消息时刷新ListView
                    msgListView.setSelection(msgList.size()); // 将ListView定位到最后一行
                    inputText.setText(""); // 清空输入框中的内容
                    sendmsg(content);
                }
                break;
            case  R.id.back:
                new AlertDialog(mContext,MainActivity.this).exitApp();
                break;
        }
    }

    private void initMsgs() {
//        Msg msg1 = new Msg("你好，家伙！", Msg.TYPE_RECEIVED);
//        msgList.add(msg1);
//        Msg msg2 = new Msg("你好，你是谁？", Msg.TYPE_SENT);
//        msgList.add(msg2);
//        Msg msg3 = new Msg("我是汤姆，很高兴认识你。", Msg.TYPE_RECEIVED);
//        msgList.add(msg3);
//        Msg msg4 = new Msg("我是杰克，也很高兴认识你。", Msg.TYPE_SENT);
//        msgList.add(msg4);

        Map<String,String> map = new HashMap<>();
        map.put("gustid",helper.getValue("mguestid"));

        HttpClientUtil.doPost("http://sit99srv.huaruntong.cn/onlinechat/hprongyun.asmx/ChatTimer", map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Msg msg = Manager.getObj(result,Msg.class);
                msg.setType(Msg.TYPE_RECEIVED);
                msgList.add(msg);
                adapter.notifyDataSetChanged(); // 当有新消息时刷新ListView
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(mContext,"接收失败",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitApp();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void sendmsg(String content){
        Map<String,String> map = new HashMap<>();
        map.put("fromid",helper.getValue("mguestid"));
        map.put("toid",helper.getValue("mguestid"));
        map.put("content",content);
        HttpClientUtil.doPost("http://sit99srv.huaruntong.cn/onlinechat/hprongyun.asmx/SendMessage", map, new Callback.CommonCallback<String>() {
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

    public void exitApp() {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(mContext);
        alertDialog.setMessage("您确定退出聊天?")
                .setPositiveButton("取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                toLogout();
            }
        });
        alertDialog.create().show();
    }

    /**
     * 退出登录
     * @param
     */
    public void toLogout(){
        Map<String,String> map = new HashMap<>();
        map.put("gustid",helper.getValue("mguestid"));
        HttpClientUtil.doPost("http://sit99srv.huaruntong.cn/onlinechat/hprongyun.asmx/endAgent", map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(mContext,"退出登录成功",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(mContext,"退出登录失败",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {

            }
        });
    }



}
