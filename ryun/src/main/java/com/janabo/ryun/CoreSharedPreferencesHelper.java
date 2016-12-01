package com.janabo.ryun;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 本地简单数据保存
 * 作者：janabo on 2016/11/26 16:41
 */
public class CoreSharedPreferencesHelper {

    private static final String PREFS_NAME = "com.janabo.db";

    private SharedPreferences settings;
    /**
     * 构造方法.
     * @param context Context
     */
    public CoreSharedPreferencesHelper(Context context) {
        settings = context.getSharedPreferences(PREFS_NAME, 0);
    }

    /**
     * 保存数据.
     * @param key key
     * @param value 值
     */
    public void setValue(String key, String value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 获取数据.
     * @param key key
     * @return  值
     */
    public String getValue(String key) {
        String hello = settings.getString(key, null);
        return hello;
    }
}
