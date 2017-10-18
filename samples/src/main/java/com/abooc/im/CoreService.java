package com.abooc.im;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.abooc.im.message.CallMessage;
import com.abooc.util.Debug;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.facetime.FaceTime;
import com.google.gson.Gson;

import static com.abooc.im.message.CallMessage.ACTION_CALL;
import static com.abooc.im.message.CallMessage.ACTION_HANG_UP;
import static com.abooc.im.message.CallMessage.ACTION_HOLD_ON;

public class CoreService extends Service {

    public static void launch(Context context) {
        Intent intent = new Intent(context, CoreService.class);
        context.startService(intent);
    }

    public CoreService() {
    }

    CustomMessageHandler iCustomMessageHandler = new CustomMessageHandler();

    @Override
    public void onCreate() {
        Debug.anchor();
        super.onCreate();
        // 这是使用美国节点
//        AVOSCloud.useAVCloudUS();
        LeanCloud.initLeanCloudSDK(this);
        LeanCloud.installAppAttributes(this.getApplicationContext());
        AVIMMessageManager.registerAVIMMessageType(CallMessage.class);
        AVIMMessageManager.registerMessageHandler(CallMessage.class, iCustomMessageHandler);

        LeanCloud.subscribePush(this.getApplicationContext());

        String uid = Saver.read(this);
        if (TextUtils.isEmpty(uid)) {
        } else {
            LeanCloud.getInstance().createClient(uid);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Debug.anchor();

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Debug.anchor();
        super.onTaskRemoved(rootIntent);

    }


    @Override
    public void onDestroy() {
        Debug.anchor();
        super.onDestroy();
    }

    public class CustomMessageHandler extends AVIMMessageHandler {
        //接收到消息后的处理逻辑
        @Override
        public void onMessage(AVIMMessage avimMessage, AVIMConversation conversation, AVIMClient client) {
            Debug.anchor(new Gson().toJson(avimMessage));
            if (avimMessage instanceof CallMessage) {
                CallMessage message = (CallMessage) avimMessage;
                int i = message.getAction();
                switch (i) {
                    case ACTION_CALL:
                        FaceTime.Companion.show(CoreService.this, message.getFrom(), CallMessage.ACTION_CALL);
                        break;
                    case ACTION_HOLD_ON:
                        break;
                    case ACTION_HANG_UP:
                        break;
                }
            } else {
                Debug.anchor(new Gson().toJson(avimMessage));
            }
        }

        public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
            Debug.error();
        }

    }

}
