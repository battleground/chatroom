package com.abooc.im;

import android.app.Application;
import android.content.Context;

import com.abooc.util.Debug;
import com.abooc.widget.Toast;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.leancloud.im.chatroom.BuildConfig;
import com.leancloud.im.chatroom.MessageHandler;

/**
 * Created by zhangxiaobo on 15/4/15.
 */
public class AppApplication extends Application {

    // "直播间聊天系统" 测试key
    public final static String LEANCLOUD_APP_ID  = "Ly7xoqX3c4P8hn7pYQoDaxXv-gzGzoHsz";
    public final static  String LEANCLOUD_APP_KEY = "KjaBxRRx7H6udI3VMRGde4tl";
    public final static String CONVERSATION_ID = "58411255128fe1005898c163"; // 普通会话
    // test
//    public final static String LEANCLOUD_APP_ID  = "p96jQI9whtwV57DptXlMBEWj-gzGzoHsz";
//    public final static  String LEANCLOUD_APP_KEY = "9hVWh7D8Fxq4vxnuh4zKC9f8";
//    public final static String CONVERSATION_ID = "5849193eac502e006c584813";

    @Override
    public void onCreate() {
        Debug.enable(BuildConfig.DEBUG);
        Toast.init(this);
        super.onCreate();

        // 这是使用美国节点
//        AVOSCloud.useAVCloudUS();
        initLeanCloudSDK(this);
    }

    public static void initLeanCloudSDK(Context context) {
        AVOSCloud.initialize(context, LEANCLOUD_APP_ID, LEANCLOUD_APP_KEY);
        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new MessageHandler(context));
    }
}
