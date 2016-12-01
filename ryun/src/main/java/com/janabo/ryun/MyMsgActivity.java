package com.janabo.ryun;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.janabo.ryun.http.HttpClientUtil;

import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * 作者：janabo on 2016/11/29 20:01
 */
public class MyMsgActivity extends AppCompatActivity {
    @BindView(R.id.txt1)
    TextView mTitle;
    @BindView(R.id.back)
    RelativeLayout mBack;
    Context mContext = this;
    CoreSharedPreferencesHelper helper;

    /**
     * 目标 Id
     */
    private String mTargetId;

    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        ButterKnife.bind(this);
        helper = new CoreSharedPreferencesHelper(this);
        Intent intent = getIntent();
        setActionBar();
        getIntentDate(intent);
        isReconnect(intent);
    }

    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {

        mTargetId = intent.getData().getQueryParameter("targetId");
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

        enterFragment(mConversationType, mTargetId);
        setActionBarTitle(mTargetId);
    }

    /**
     * 加载会话页面 ConversationFragment
     *
     * @param mConversationType
     * @param mTargetId
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {
  //      MySendConversationFragment fragment = new MySendConversationFragment();
        ConversationFragment fragment = new ConversationFragment();
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);
//        fragment.setFromid(helper.getValue("mguestid"));
//        fragment.setToid(mTargetId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //xxx 为你要加载的 id
        transaction.add(R.id.rong_content, fragment);
        transaction.commit();

        if (RongIM.getInstance() != null) {
            //设置自己发出的消息监听器。
            RongIM.getInstance().setSendMessageListener(new MySendMessageListener(mContext));
        }
    }

    /**
     * 判断消息是否是 push 消息
     */
    private void isReconnect(Intent intent) {

        String token = helper.getValue("mtoken");
        //push或通知过来
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {

            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
            if (intent.getData().getQueryParameter("push") != null
                    && intent.getData().getQueryParameter("push").equals("true")) {

                reconnect(token);
            } else {
                //程序切到后台，收到消息后点击进入,会执行这里
                if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {
                    reconnect(token);
                } else {
                    enterFragment(mConversationType, mTargetId);
                }
            }
        }
    }


    /**
     * 设置 actionbar 事件
     */
    private void setActionBar() {

        mTitle = (TextView) findViewById(R.id.txt1);
        mBack = (RelativeLayout) findViewById(R.id.back);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



    /**
     * 设置 actionbar title
     */
    private void setActionBarTitle(String targetid) {

        mTitle.setText(targetid);
    }

    /**
     * 重连
     *
     * @param token
     */
    private void reconnect(String token) {

        if (getApplicationInfo().packageName.equals(MyApplication.getCurProcessName(getApplicationContext()))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {

                }

                @Override
                public void onSuccess(String s) {
                    Log.d("MyMsgActivity", "--onSuccess---" + s);
                    enterFragment(mConversationType, mTargetId);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }


    /**
     * 退出登录
     * @param
     */
    public void toLogout(){
        Map<String,String> map = new HashMap<>();
        map.put("gustid",helper.getValue("mguestid"));
        HttpClientUtil.doPost("http://192.168.3.68:8193/hprongyun.asmx/endAgent", map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                RongIM.getInstance().logout();
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

    @Override
    public void onBackPressed() {
        toLogout();
        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.rong_content);
        if(!fragment.onBackPressed()) {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        onBackPressed();
        return super.onKeyDown(keyCode, event);
    }
}
