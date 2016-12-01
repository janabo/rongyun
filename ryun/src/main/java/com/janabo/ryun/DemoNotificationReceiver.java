package com.janabo.ryun;

import android.content.Context;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * 作者：janabo on 2016/11/30 10:25
 */
public class DemoNotificationReceiver extends PushMessageReceiver {
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage message) {
        return false;
    }
}
