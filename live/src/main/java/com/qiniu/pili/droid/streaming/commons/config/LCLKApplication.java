package com.qiniu.pili.droid.streaming.commons.config;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.abooc.util.Debug;
import com.abooc.widget.Toast;
import com.leancloud.im.chatroom.BuildConfig;
import com.leancloud.im.chatroom.AppApplication;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/12/5.
 */

public class LCLKApplication extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        setOkHttp();
        // 初始化IM和debug工具
        AppApplication.initLeanCloudSDK(this);
        Debug.enable(BuildConfig.DEBUG);
        Toast.init(this);
    }

    /**
     * 获取 getApplicationContext
     */
    public static Context getAppContext(){
        return appContext;
    }

    /**
     *
     * @return
     */
    public static boolean hasNetwork() {
        if (appContext != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 设置OkHttp
     */
    private void setOkHttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("JS"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

}
