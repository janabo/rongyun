package com.janabo.myim.http;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

/**
 * 作者：janabo on 2016/11/26 16:27
 */
public class HttpClientUtil {

    public static void doPost(String url, Map<String, String> map, Callback.CommonCallback<String> callBack) {
        RequestParams params = new RequestParams(url);
        if (map != null) {
            for (Map.Entry<String, String> entry :  map.entrySet()) {
                params.addBodyParameter(entry.getKey(), entry.getValue());
            }
        }

        x.http().post(params,callBack);
}


}
