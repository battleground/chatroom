package com.abooc.im;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;

import com.abooc.im.activity.LoginActivity;
import com.abooc.util.Debug;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMClientEventHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dayu on 2017/6/8.
 */

public class LeanCloud {


    // "直播间聊天系统" 测试key
    public final static String LEANCLOUD_APP_ID = "Ly7xoqX3c4P8hn7pYQoDaxXv-gzGzoHsz";
    public final static String LEANCLOUD_APP_KEY = "KjaBxRRx7H6udI3VMRGde4tl";
    public final static String CONVERSATION_ID = "58411255128fe1005898c163"; // 005
    public final static String CONVERSATION_ID_2 = "583ff241ac502e006cbc626f"; // 006

    public final static String CONVERSATION_ID_TOM_JERRY = "592fbc5c1b69e6005ca9c156"; // 聊天会话
    public final static String CONVERSATION_ID_TOM_JERRY_SYSTEM = "5874e1212f301e006be81720"; // 通知会话

    // test
//    public final static String LEANCLOUD_APP_ID  = "p96jQI9whtwV57DptXlMBEWj-gzGzoHsz";
//    public final static  String LEANCLOUD_APP_KEY = "9hVWh7D8Fxq4vxnuh4zKC9f8";
//    public final static String CONVERSATION_ID = "58aad41761ff4b006b59dce0";
//    public final static String CONVERSATION_ID_2 = "58aad415570c35006b5517b4"; // 电影纵贯线
//    public final static String CONVERSATION_ID_2 = "589aab5861ff4b0058dc30d3"; // 大胃王密子君会话

    public static final String PLATFORM_TV = "TV";
    public static final String PLATFORM_MOBILE = "Mobile";
    public static String PLATFORM = PLATFORM_MOBILE;

    private String LC_CLIENT = LoginActivity.LEANCOUND_CLIENT_TOM;
    private boolean mOnline = false;
    private AVIMClient mAVIMClient;

    private static final LeanCloud ourInstance = new LeanCloud();

    public static LeanCloud getInstance() {
        return ourInstance;
    }

    private LeanCloud() {
        AVIMClient.setClientEventHandler(new AVIMClientEventHandler() {
            @Override
            public void onConnectionPaused(AVIMClient avimClient) {
                mOnline = false;
                Debug.error("网络断开！");
                for (AVIMClientEventHandler handler : mAVIMClientEventHandlers) {
                    handler.onConnectionPaused(avimClient);
                }
            }

            @Override
            public void onConnectionResume(AVIMClient avimClient) {
                mOnline = true;
                Debug.error("");

                for (AVIMClientEventHandler handler : mAVIMClientEventHandlers) {
                    handler.onConnectionResume(avimClient);
                }
            }

            @Override
            public void onClientOffline(AVIMClient avimClient, int i) {
                Debug.error(avimClient.getClientId() + ", code:" + i + ", 您的账号正在另一台设备登录");
                mOnline = false;
                for (AVIMClientEventHandler handler : mAVIMClientEventHandlers) {
                    handler.onClientOffline(avimClient, i);
                }
            }
        });
    }

    private List<AVIMClientEventHandler> mAVIMClientEventHandlers = new ArrayList<>();

    public void addAVIMClientEventHandler(AVIMClientEventHandler handler) {
        if (handler != null)
            mAVIMClientEventHandlers.add(handler);
    }

    public void removeAVIMClientEventHandler(AVIMClientEventHandler handler) {
        if (handler != null)
            mAVIMClientEventHandlers.remove(handler);
    }

    public AVIMClient createClient(String clientId) {
        LC_CLIENT = clientId;

        mAVIMClient = AVIMClient.getInstance(clientId, PLATFORM);
        return mAVIMClient;
    }

    public String getClientId() {
        return LC_CLIENT;
    }

    public boolean isOnline() {
        return mOnline;
    }


    public static AlertDialog alert(Activity activity, DialogInterface.OnClickListener l) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("异地登录")
                .setMessage("您的账号正在另一台设备登录")
                .setCancelable(false)
                .setPositiveButton("确定退出", l);
        return builder.create();
    }


    public static void initLeanCloudSDK(Context context) {
        AVOSCloud.initialize(context, LEANCLOUD_APP_ID, LEANCLOUD_APP_KEY);
        AVInstallation avInstallation = AVInstallation.getCurrentInstallation();
//        avInstallation.put("platform", PLATFORM);
        avInstallation.put("model", Build.BRAND + " " + Build.MODEL);
        avInstallation.put("packageName", context.getPackageName());
        avInstallation.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Debug.anchor();
                } else {
                    Debug.error("saveInBackground: " + e);
                }

            }
        });
    }

    public static void delete() {
        AVInstallation avInstallation = AVInstallation.getCurrentInstallation();
        avInstallation.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Debug.anchor();
                } else {
                    Debug.error("deleteInBackground: " + e);
                }

            }
        });
    }

    public void online(final boolean online) {
        AVInstallation avInstallation = AVInstallation.getCurrentInstallation();
        avInstallation.put("platform", PLATFORM);
        avInstallation.put("uid", LC_CLIENT);
        avInstallation.put("online", online ? "online" : "offline");
        avInstallation.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    mOnline = online;
                    Debug.anchor();
                } else {
                    Debug.error("online(): " + e);
                }
            }
        });
    }
}
