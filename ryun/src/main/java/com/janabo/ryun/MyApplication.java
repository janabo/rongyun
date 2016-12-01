package com.janabo.ryun;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import org.xutils.x;

import io.rong.imkit.RongIM;

/**
 * 作者：janabo on 2016/11/26 16:08
 */
public class MyApplication extends Application{

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        x.Ext.init(this);//xutil初始化
        x.Ext.setDebug(true);// 设置是否输出debug
//        /**
//         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIMClient 的进程和 Push 进程执行了 init。
//         * io.rong.push 为融云 push 进程名称，不可修改。
//         */
//        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
//                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
//
//            /**
//             * IMKit SDK调用第一步 初始化
//             */
//            RongIM.init(this);
//
////            RongIMClient.init(this);
//        }

        RongIM.init(this);

    }

    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

}
