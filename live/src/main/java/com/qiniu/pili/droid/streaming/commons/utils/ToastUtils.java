package com.qiniu.pili.droid.streaming.commons.utils;

import android.view.View;
import android.widget.Toast;

import com.qiniu.pili.droid.streaming.commons.config.LCLKApplication;
import com.qiniu.pili.droid.streaming.demo.R;

public class ToastUtils {

    private static Toast toast;

    public static void showToast(String msg) {
        if (toast == null) {
            synchronized (ToastUtils.class) {
                if (toast == null) {
                    toast = new Toast(LCLKApplication.getAppContext());
                    View view = View.inflate(LCLKApplication.getAppContext(), R.layout.default_toast_view, null);
                    toast.setView(view);
                    toast.setDuration(Toast.LENGTH_SHORT);
                }
            }
        }
        toast.setText(msg);
        toast.show();
    }

    public static void showToastLong(String msg) {
        if (toast == null) {
            synchronized (ToastUtils.class) {
                if (toast == null) {
                    toast = new Toast(LCLKApplication.getAppContext());
                    View view = View.inflate(LCLKApplication.getAppContext(), R.layout.default_toast_view, null);
                    toast.setView(view);
                    toast.setDuration(Toast.LENGTH_LONG);
                }
            }
        }
        toast.setText(msg);
        toast.show();
    }

    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }


}
