package com.abooc.im;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.abooc.im.activity.OfflineAlert;
import com.abooc.util.Debug;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMClientEventHandler;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationEventHandler;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.facetime.FaceTimeActivity;
import com.facetime.CallOutActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dayu on 2017/6/8.
 */

public class LeanCloud {

    public static final String PLATFORM_TV = "TV";
    public static final String PLATFORM_MOBILE = "mobile";
    public static String PLATFORM = PLATFORM_MOBILE;

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
                LeanCloud.getInstance().online(false);
                mOnline = false;
                for (AVIMClientEventHandler handler : mAVIMClientEventHandlers) {
                    handler.onClientOffline(avimClient, i);
                }
                OfflineAlert.show(AppApplication.getContext());
            }
        });
    }

    public AVIMClient createClient(String clientId) {
        AVIMMessageManager.setConversationEventHandler(new CustomConversationEventHandler());

        mAVIMClient = AVIMClient.getInstance("86 " + clientId, PLATFORM);
        return mAVIMClient;
    }

    public void addLogoutListener(OnLogoutListener l) {
        mOnLogoutListener = l;
    }

    public void removeLogoutListener(OnLogoutListener l) {
        mOnLogoutListener = l;
    }

    OnLogoutListener mOnLogoutListener;

    public interface OnLogoutListener {
        void onLogout();
    }

    /**
     * 注销账号
     */
    public void logout() {
        mAVIMClient.close(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (e == null) {
                    if (mOnLogoutListener != null) mOnLogoutListener.onLogout();
                } else {
                    String clientId = mAVIMClient.getClientId();
                    Debug.error(clientId + " 退出失败！" + e);
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

    public class CustomConversationEventHandler extends AVIMConversationEventHandler {

        @Override
        public void onMemberLeft(AVIMClient client, AVIMConversation conversation, List<String> members,
                                 String kickedBy) {
            // 有其他成员离开时，执行此处逻辑
            Debug.anchor(members + ", " + conversation.getConversationId() + "；操作者为： " + kickedBy);
        }

        @Override
        public void onMemberJoined(AVIMClient client, AVIMConversation conversation,
                                   List<String> members, String invitedBy) {
            Debug.anchor(members + "加入到" + conversation.getConversationId() + "；操作者为： " + invitedBy);

        }

        @Override
        public void onKicked(AVIMClient client, AVIMConversation conversation, String kickedBy) {
            // 当前 ClientId(Bob) 被踢出对话，执行此处逻辑
            Debug.anchor(client.getClientId() + ", " + conversation.getConversationId() + "；操作者为： " + kickedBy);
        }

        @Override
        public void onInvited(AVIMClient client, AVIMConversation conversation, String invitedBy) {
            // 当前 ClientId(Bob) 被邀请到对话，执行此处逻辑
            Debug.anchor(client.getClientId() + ", " + conversation.getConversationId() + "；操作者为： " + invitedBy);
        }

        @Override
        public void onUnreadMessagesCountUpdated(AVIMClient client, AVIMConversation conversation) {
            Debug.anchor(client.getClientId() + ", " + conversation.getConversationId() + ", 未读消息数：" + conversation.getUnreadMessagesCount());
        }
    }

    public String getClientId() {
        return mAVIMClient.getClientId();
    }

    public boolean isOnline() {
        return mOnline;
    }

    public static void initLeanCloudSDK(Context context) {
        AVOSCloud.setDebugLogEnabled(true);
        AVOSCloud.initialize(context, LcConfig.LEANCLOUD_APP_ID, LcConfig.LEANCLOUD_APP_KEY);
        LcConfig.xPush(context);
        AVIMClient.setOfflineMessagePush(true);
        AVIMClient.setAutoOpen(true);
    }

    public static void installAppAttributes(Context context) {
        AVInstallation avInstallation = AVInstallation.getCurrentInstallation();
//        avInstallation.put("platform", PLATFORM);
        avInstallation.put("model", Build.BRAND + " " + Build.MODEL);
        avInstallation.put("packageName", context.getPackageName());

        subscribePush(context);

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


    public static String[] getChannels() {
        AVInstallation avInstallation = AVInstallation.getCurrentInstallation();
        JSONArray jsonArray = avInstallation.getJSONArray("channels");
        if (jsonArray != null && jsonArray.length() > 0) {
            String[] channels = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    channels[i] = jsonArray.getString(i);
                } catch (JSONException e) {
                    Debug.error(e);
                }
            }
            return channels;
        }
        return new String[0];
    }

    /**
     * 订阅LeanCloud实时推送
     *
     * @param context
     */
    public static void subscribePush(Context context) {
        // 显示的设备的 installationId，用于推送的设备标示

        String[] channels = getChannels();
        AVInstallation.getCurrentInstallation().removeAll("channels", Arrays.asList(channels));

        context = context.getApplicationContext();
        String versionName = getVersionName(context);

        PushService.setDefaultPushCallback(context, FaceTimeActivity.class);
        PushService.subscribe(context, "version-" + versionName, CallOutActivity.class);
        PushService.subscribe(context, BuildConfig.DEBUG ? "dev" : "release", CallOutActivity.class);
        PushService.subscribe(context, "update", CallOutActivity.class);
    }

    public void online(final boolean online) {
        AVInstallation avInstallation = AVInstallation.getCurrentInstallation();
        avInstallation.put("platform", PLATFORM);
        avInstallation.put("uid", getClientId());
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

    public static String getVersionName(Context context) {
        PackageManager packagemanager = context.getPackageManager();
        String packName = context.getPackageName();
        try {
            PackageInfo packageinfo = packagemanager.getPackageInfo(packName, 0);
            return packageinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
