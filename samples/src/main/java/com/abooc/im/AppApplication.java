package com.abooc.im;

import android.app.Application;
import android.content.Context;

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
        Toast.init(this);

        About.defaultAbout(this);

        // 这是使用美国节点
//        AVOSCloud.useAVCloudUS();
        LeanCloud.initLeanCloudSDK(this);
    }

    public static Context getContext() {
        return mContext;
    }

}
