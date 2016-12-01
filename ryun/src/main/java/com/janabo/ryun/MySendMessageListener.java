package com.janabo.ryun;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.janabo.ryun.http.HttpClientUtil;

import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * 作者：janabo on 2016/12/1 19:40
 */
public class MySendMessageListener implements RongIM.OnSendMessageListener {
    Context mContext;
    CoreSharedPreferencesHelper helper ;

    /**
     * 消息发送前监听器处理接口（是否发送成功可以从 SentStatus 属性获取）。
     *
     * @param message 发送的消息实例。
     * @return 处理后的消息实例。
     */
    @Override
    public Message onSend(Message message) {
        //开发者根据自己需求自行处理逻辑
        return message;
    }

    public MySendMessageListener(Context mContext) {
        this.mContext = mContext;
        helper = new CoreSharedPreferencesHelper(mContext);
    }

    /**
     * 消息在 UI 展示后执行/自己的消息发出后执行,无论成功或失败。
     *
     * @param message              消息实例。
     * @param sentMessageErrorCode 发送消息失败的状态码，消息发送成功 SentMessageErrorCode 为 null。
     * @return true 表示走自已的处理方式，false 走融云默认处理方式。
     */
    @Override
    public boolean onSent(Message message,RongIM.SentMessageErrorCode sentMessageErrorCode) {

        if(message.getSentStatus()== Message.SentStatus.FAILED){
            if(sentMessageErrorCode== RongIM.SentMessageErrorCode.NOT_IN_CHATROOM){
                //不在聊天室
            }else if(sentMessageErrorCode== RongIM.SentMessageErrorCode.NOT_IN_DISCUSSION){
                //不在讨论组
            }else if(sentMessageErrorCode== RongIM.SentMessageErrorCode.NOT_IN_GROUP){
                //不在群组
            }else if(sentMessageErrorCode== RongIM.SentMessageErrorCode.REJECTED_BY_BLACKLIST){
                //你在他的黑名单中
            }
        }

        MessageContent messageContent = message.getContent();

        if (messageContent instanceof TextMessage) {//文本消息
            TextMessage textMessage = (TextMessage) messageContent;
            sendmsg(textMessage.getContent());
            Log.i("MySendMessageListener", "onSent-TextMessage:" + textMessage.getContent());
        } else if (messageContent instanceof ImageMessage) {//图片消息
            ImageMessage imageMessage = (ImageMessage) messageContent;
            Log.i("MySendMessageListener", "onSent-ImageMessage:" + imageMessage.getRemoteUri());
        } else if (messageContent instanceof VoiceMessage) {//语音消息
            VoiceMessage voiceMessage = (VoiceMessage) messageContent;
            sendmsg( voiceMessage.getUri().toString());
            Log.i("MySendMessageListener", "onSent-voiceMessage:" + voiceMessage.getUri().toString());
        } else if (messageContent instanceof RichContentMessage) {//图文消息
            RichContentMessage richContentMessage = (RichContentMessage) messageContent;
            Log.i("MySendMessageListener", "onSent-RichContentMessage:" + richContentMessage.getContent());
            sendmsg(richContentMessage.getContent());
        } else {
            Log.i("MySendMessageListener", "onSent-其他消息，自己来判断处理");
        }

        return false;
    }

    public void sendmsg(String content){
        Map<String,String> map = new HashMap<>();
        map.put("fromid",helper.getValue("mguestid"));
        map.put("toid",helper.getValue("mguestid"));
        map.put("content",content);

        HttpClientUtil.doPost("http://192.168.3.68:8193/hprongyun.asmx/SendMessage", map, new Callback.CommonCallback<String>() {
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

}
