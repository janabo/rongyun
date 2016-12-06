package com.janabo.myim;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.janabo.myim.entity.GuestInfo;
import com.janabo.myim.http.HttpClientUtil;
import com.janabo.myim.http.Manager;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：janabo on 2016/12/5 14:19
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {
    Context mContext = this;
    @ViewInject(R.id.title)
    TextView title;
    @ViewInject(R.id.uid)
    MaterialEditText uid;
    @ViewInject(R.id.psw)
    MaterialEditText psw;
    @ViewInject(R.id.ok)
    Button ok;

    CoreSharedPreferencesHelper helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("登录");
        helper = new CoreSharedPreferencesHelper(this);
    }
    @Event(value={R.id.ok})
    private void getEvent(View view){
        switch(view.getId()){
            case R.id.ok:
                InputMethodManager imm = (InputMethodManager) LoginActivity.this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                checkLogin();
                break;
        }
    }

    private void checkLogin(){
        // 清除错误提示
        uid.setError(null);
        psw.setError(null);
        String userId = uid.getText().toString().trim();
        String pass = psw.getText().toString().trim();
        boolean cancel = false;
        View focusView = null;

        // 校验用户名是否为空
        if (TextUtils.isEmpty(userId)) {
            uid.setError(getString(R.string.nullUid));
            focusView = uid;
            cancel = true;
        }

        if (TextUtils.isEmpty(pass) && !cancel) {
            psw.setError(getString(R.string.nullPsw));
            focusView = psw;
            cancel = true;
        }
        if (cancel) {//true为出现错误
            focusView.requestFocus();
        } else {
            login();
        }
    }

    private void login(){
        Map<String,String> map = new HashMap<>();
        map.put("Ip",uid.getText().toString());
        map.put("guest_name",psw.getText().toString());
        map.put("urlref","");
        HttpClientUtil.doPost("http://sit99srv.huaruntong.cn/onlinechat/hprongyun.asmx/Init_Guest_Info", map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                GuestInfo guestInfo = Manager.getObj(result, GuestInfo.class);
                helper.setValue("mtoken",guestInfo.getToken());
                helper.setValue("mguestid",guestInfo.getGuest_id());
                Toast.makeText(mContext,"登录成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext,MainActivity.class);
                startActivity(intent);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
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
}
