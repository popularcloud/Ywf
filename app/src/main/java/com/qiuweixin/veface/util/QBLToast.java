package com.qiuweixin.veface.util;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qiuweixin.veface.R;
import com.qiuweixin.veface.base.App;

public class QBLToast {

    private static Handler uiHandler = new Handler(Looper.getMainLooper());
    private static Toast toast;

    public static void show(int res) {
        String text = App.self.getString(res);
        show(text);
    }

    public static void show(final String text) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            doShow(text);
        } else {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    doShow(text);
                }
            });
        }
    }

    public static void cancel() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            doCancel();
        } else {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    doCancel();
                }
            });
        }
    }

    private static void doCancel() {
        if (toast != null) {
            toast.cancel();
        }
    }

    private static void doShow(String text) {
        //取消显示上一个Toast
        doCancel();

        Context context = App.self;

        toast = new Toast(context);

        toast.setDuration(Toast.LENGTH_SHORT);

        toast.setGravity(Gravity.TOP, 0, context.getResources().getDimensionPixelOffset(R.dimen.toolbar_height));

        View view = View.inflate(context, R.layout.toast, null);
        toast.setView(view);

        TextView textView = (TextView) view.findViewById(R.id.textMessage);
        textView.setText(text);

        toast.show();
    }
}
