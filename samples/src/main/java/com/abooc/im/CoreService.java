package com.abooc.im;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.abooc.im.activity.Claim;
import com.abooc.im.message.GiftMessage;
import com.abooc.util.Debug;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.facetime.CallIn;
import com.google.gson.Gson;

import java.util.Locale;

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
        AVIMMessageManager.registerAVIMMessageType(GiftMessage.class);
        AVIMMessageManager.registerMessageHandler(GiftMessage.class, iCustomMessageHandler);

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


    Gson mGson = new Gson();

    public class CustomMessageHandler extends AVIMMessageHandler {
        //接收到消息后的处理逻辑
        @Override
        public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
            if (message instanceof GiftMessage) {
                CallIn.Companion.show(CoreService.this);
//                GiftMessage giftMessage = (GiftMessage) message;

//                addToContainer(giftMessage);
//                charge(giftMessage);

//                switch (giftMessage.getMoney()) {
//                    case 100:
////                        doGift(giftMessage);
//                        break;
//                    case 1000:
//                        doBigGift(giftMessage);
//                        break;
//                }
            } else {
                Debug.anchor(mGson.toJson(message));
            }
        }

        public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
            Debug.error();
        }

//        void addToContainer(GiftMessage giftMessage) {
//            int index = giftMessage.getGiftIndex();
//            String giftIndex = index > 1 ? " X " + index : "";
//            String messageString = "【" + giftMessage.getUid() + "】：" + giftMessage.getName() + giftIndex;
//            CharSequence text = mMessageText.getText();
//            String time = toTime(giftMessage.getTimestamp());
//            mMessageText.setText(text + "\n" + time + messageString);
//        }

//        void doGift(GiftMessage giftMessage) {
//            if (giftMessage.getGiftIndex() > 1) {
//                mXText.startAnimation(mAnimationX);
//                mXText.setText("X " + giftMessage.getGiftIndex());
//            } else {
//                mXText.setText(null);
//            }
//        }

        void doBigGift(GiftMessage giftMessage) {
            Claim.show(CoreService.this);
        }

        String toTime(long time) {
            java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
            return format1.format(time);
        }
    }

}
