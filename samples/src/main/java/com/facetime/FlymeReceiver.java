package com.facetime;

import android.content.Context;
import android.content.Intent;

import com.abooc.im.message.CallMessage;
import com.abooc.util.Debug;
import com.avos.avoscloud.AVFlymePushMessageReceiver;
import com.meizu.cloud.pushsdk.notification.PushNotificationBuilder;

public class FlymeReceiver extends AVFlymePushMessageReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Debug.error(intent.toString() + "\n"
        + intent.getExtras().toString());
//        super.onReceive(context, intent);

        FaceTime.Companion.show(context, "- -", CallMessage.ACTION_CALL);
    }


    @Override
    public void onMessage(Context context, String s) {
        Debug.error();
        super.onMessage(context, s);
    }

    public void onUpdateNotificationBuilder(PushNotificationBuilder var1) {
        Debug.error();
    }

    public void onNotificationClicked(Context var1, String var2, String var3, String var4) {
        Debug.error();
    }

    public void onNotificationArrived(Context var1, String var2, String var3, String var4) {
        Debug.error();
    }

    public void onNotificationDeleted(Context var1, String var2, String var3, String var4) {
        Debug.error();
    }

    public void onNotifyMessageArrived(Context var1, String var2) {
        Debug.error();
    }
}
