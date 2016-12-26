package com.abooc.liveroom;

import android.app.Application;
import android.content.Context;

import com.abooc.util.Debug;
import com.abooc.widget.Toast;
import com.qiniu.pili.droid.streaming.StreamingEnv;

/**
 * Created by jerikc on 16/4/14.
 */
public class AppApplication extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        StreamingEnv.init(getApplicationContext());
        Debug.enable(BuildConfig.DEBUG);
        Toast.init(this);
    }

    /**
     * 获取 getApplicationContext
     */
    public static Context getAppContext() {
        return appContext;
    }

}
