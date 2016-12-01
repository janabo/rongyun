package com.janabo.ryun;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.janabo.ryun.entity.GuestInfo;
import com.janabo.ryun.http.HttpClientUtil;
import com.janabo.ryun.http.Manager;

import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

public class MainActivity extends AppCompatActivity {
    Context mContext = this;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.sendmsg)
    Button sendMsg;
    @BindView(R.id.logout)
    Button logout;
    @BindView(R.id.content)
    TextView content;
    CoreSharedPreferencesHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        helper = new CoreSharedPreferencesHelper(this);
        RongIMClient.setOnReceiveMessageListener(new MyReceiveMessageListener());
    }

    @OnClick(R.id.login)
    public void toLogin(View v){
        Map<String,String> map = new HashMap<>();
        map.put("Ip","system");
        map.put("guest_name","system");
        map.put("urlref","");
        HttpClientUtil.doPost("http://192.168.3.68:8193/hprongyun.asmx/Init_Guest_Info", map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                GuestInfo guestInfo = Manager.getObj(result, GuestInfo.class);
                connect(guestInfo.getToken());
                helper.setValue("mtoken",guestInfo.getToken());
                helper.setValue("mguestid",guestInfo.getGuest_id());
                content.setText("登录成功");
                Toast.makeText(mContext,"登录成功",Toast.LENGTH_SHORT).show();

                //启动会话界面 //登录成功跳转到聊天
                if (RongIM.getInstance() != null)
                    RongIM.getInstance().startPrivateChat(MainActivity.this, "system", "title");
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                content.setText(ex.getMessage());
                Toast.makeText(mContext,"登录失败",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
        });
    }

    @OnClick(R.id.sendmsg)
    public void sendmsg(View v){
        Map<String,String> map = new HashMap<>();
        map.put("fromid",helper.getValue("mguestid"));
        map.put("toid",helper.getValue("mguestid"));
        map.put("content","测试啊");

        setSendMsgToRongyun(helper.getValue("mguestid"),"测试啊");
        HttpClientUtil.doPost("http://192.168.3.68:8193/hprongyun.asmx/SendMessage", map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Toast.makeText(mContext,"发送成功",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                content.setText(ex.getMessage());
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


    /**
     * 建立融云服务器连接
     * @param token
     */
    private void connect(String token) {
        if (getApplicationInfo().packageName.equals(MyApplication.getCurProcessName(getApplicationContext()))) {
            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIMClient.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
                 */
                @Override
                public void onTokenIncorrect() {

                    Log.d("LoginActivity", "--onTokenIncorrect");
                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token
                 */
                @Override
                public void onSuccess(String userid) {

                    Log.d("LoginActivity", "--onSuccess---" + userid);
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                    Log.d("LoginActivity", "--onError" + errorCode);
                }
            });
        }
    }

    /**
     * 发送消息到融云
     */
    public void setSendMsgToRongyun(String targetId,String mContent){
        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, targetId,
                TextMessage.obtain(mContent), null, null, new RongIMClient.SendMessageCallback() {
                    @Override
                    public void onSuccess(Integer integer) {
                        Log.d("sendMsg", "发送成功");
                        content.setText("发送成功");
                        Toast.makeText(mContext,"发送到融云成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {
                        Log.d("sendMsg", "发送失败");
                        content.setText("发送到融云失败"+errorCode);
                        Toast.makeText(mContext,"发送到融云成功",Toast.LENGTH_SHORT).show();
                    }
                }, null);
    }

    private class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {
        /**
         * 收到消息的处理。
         * @param message 收到的消息实体。
         * @param left 剩余未拉取消息数目。
         * @return
         */
        @Override
        public boolean onReceived(Message message, int left) {
         //       mHandler.sendEmptyMessage(1);
            //开发者根据自己需求自行处理
            int messageId = message.getMessageId();
            Toast.makeText(mContext,"消息id"+messageId,Toast.LENGTH_SHORT).show();
            MessageContent messageContent = message.getContent();
       //     content.setText(message.getContent().getMentionedInfo().getMentionedContent());
            return false;
        }
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            Toast.makeText(mContext,"消息id",Toast.LENGTH_SHORT).show();
        }
    };


}
