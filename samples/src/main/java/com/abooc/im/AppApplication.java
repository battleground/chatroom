package com.abooc.im;

import android.app.Application;
import android.content.Context;

import com.abooc.im.activity.LoginActivity;
import com.abooc.plugin.about.About;
import com.abooc.util.Debug;
import com.abooc.widget.Toast;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;

/**
 */
public class AppApplication extends Application {

    // "直播间聊天系统" 测试key
    public final static String LEANCLOUD_APP_ID  = "Ly7xoqX3c4P8hn7pYQoDaxXv-gzGzoHsz";
    public final static  String LEANCLOUD_APP_KEY = "KjaBxRRx7H6udI3VMRGde4tl";
    public final static String CONVERSATION_ID = "58411255128fe1005898c163"; // 005
    public final static String CONVERSATION_ID_2 = "583ff241ac502e006cbc626f"; // 006

    // test
//    public final static String LEANCLOUD_APP_ID  = "p96jQI9whtwV57DptXlMBEWj-gzGzoHsz";
//    public final static  String LEANCLOUD_APP_KEY = "9hVWh7D8Fxq4vxnuh4zKC9f8";
//    public final static String CONVERSATION_ID = "58aad41761ff4b006b59dce0";
//    public final static String CONVERSATION_ID_2 = "58aad415570c35006b5517b4"; // 电影纵贯线
//    public final static String CONVERSATION_ID_2 = "589aab5861ff4b0058dc30d3"; // 大胃王密子君会话

    public static String LC_CLIENT = LoginActivity.LEANCOUND_CLIENT_TOM;

    @Override
    public void onCreate() {
        super.onCreate();

        Debug.enable(BuildConfig.DEBUG);
        Toast.init(this);

        About.defaultAbout(this);

        // 这是使用美国节点
//        AVOSCloud.useAVCloudUS();
        initLeanCloudSDK(this);

        AVInstallation.getCurrentInstallation().saveInBackground();
    }

    public static void initLeanCloudSDK(Context context) {
        AVOSCloud.initialize(context, LEANCLOUD_APP_ID, LEANCLOUD_APP_KEY);
//        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new MessageHandler(context));
    }

//    private void a(){
//
//        AVInstallation avInstallation = AVInstallation.getCurrentInstallation();
//        .saveInBackground();
//        avInstallation.put("packageName", packName);
//
//    }
}
