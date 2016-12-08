package com.qiniu.pili.droid.streaming.demo;

import com.qiniu.pili.droid.streaming.StreamingEnv;
import com.qiniu.pili.droid.streaming.commons.config.LCLKApplication;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by jerikc on 16/4/14.
 */
public class StreamingApplication extends LCLKApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        StreamingEnv.init(getApplicationContext());
        LeakCanary.install(this);
    }
}
