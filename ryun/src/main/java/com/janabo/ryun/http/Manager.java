package com.janabo.ryun.http;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * 作者：janabo on 2016/11/26 16:38
 */
public class Manager {
    private static final Gson mGson;
    static {
        mGson = new Gson();
    }



    /**
     * 解析json为对象
     * @param result
     * @param classname
     * @param <T>
     * @return
     */
    public static <T> T getObj(String result,Class<T> classname){
        try {
            JSONObject json = getJSONObject(result);
            return mGson.fromJson(json.toString(), classname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json转换.
     *
     * @param responseInfo
     *            HttpResponse
     * @return JSONObject
     */
    public static JSONObject getJSONObject(String responseInfo) {
        try {
            if (isNotEmpty(responseInfo)) {
                if (responseInfo.toLowerCase().startsWith("<html")) {
                    return null;
                } else {
                    JSONObject json = new JSONObject(responseInfo);
                    return json;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isNotEmpty(String str) {
        boolean bool = true;
        if (str == null || "null".equals(str) || "".equals(str)) {
            bool = false;
        } else {
            bool = true;
        }
        return bool;
    }

}
