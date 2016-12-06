package com.janabo.myim;

import android.app.Application;

import org.xutils.x;

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
    }
}
