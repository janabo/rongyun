package com.janabo.myim;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * 作者：janabo on 2016/12/5 15:28
 */
public class AlertDialog {
    private Activity activity;
    private Context context;
    private Dialog dlg;

    public AlertDialog(Context context,Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    /**
     * 公共对话框.
     */
    public void exitApp() {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialog.setMessage("您确定退出聊天?")
                .setPositiveButton("取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               activity.finish();
            }
        });
        alertDialog.create().show();
    }
}
