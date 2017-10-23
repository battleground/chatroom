package com.abooc.im;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;

import com.abooc.plugin.about.About;
import com.abooc.util.Debug;
import com.abooc.widget.Toast;

/**
 */
public class AppApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
        Debug.enable(BuildConfig.DEBUG);
        Debug.error();
        Toast.init(this);

        About.defaultAbout(this);
        LeanCloud.initLeanCloudSDK(this.getApplicationContext());
        CoreService.launch(this);
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onLowMemory() {
        Debug.anchor();
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        Debug.anchor();
        super.onTrimMemory(level);
    }

    @Override
    public void onTerminate() {
        Debug.anchor();
        super.onTerminate();
    }

    public static AlertDialog alert(Context context, DialogInterface.OnClickListener l) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context.getApplicationContext());
        builder.setTitle("异地登录")
                .setMessage("您的账号正在另一台设备登录")
                .setCancelable(false)
                .setPositiveButton("确定退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        return alertDialog;
    }
}
