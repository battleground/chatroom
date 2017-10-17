package com.facetime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.abooc.util.Debug;
import com.avos.avoscloud.AVConstants;
import com.avos.avoscloud.AVMiPushMessageReceiver;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageHelper;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class XiaoMiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Debug.error(intent.getAction());
        if (intent.getAction().equals(AVConstants.AV_MIXPUSH_MI_NOTIFICATION_ACTION)) {
            processMiNotificationClickEvent(context, intent);
        } else if (intent.getAction().equals(AVConstants.AV_MIXPUSH_FLYME_NOTIFICATION_ACTION)) {
            processMiNotificationClickEvent(context, intent);
        } else {
            processCustomReceiverMessage(context, intent);
        }
    }

//    @Override
//    public void onReceive(Context context, Intent intent) {
//
//        Debug.error(intent.toString() + "\n" + intent.getExtras().toString());
//
//        MiPushMessage message = (MiPushMessage) intent.getSerializableExtra(PushMessageHelper.KEY_MESSAGE);
//        Debug.error(message.toString());
////        to(intent);
//    }


    /**
     * 处理自定义消息
     *
     * @param context
     * @param intent
     */
    private void processCustomReceiverMessage(Context context, Intent intent) {
        Toast.makeText(context, "自定义 receiver 收到消息", Toast.LENGTH_SHORT).show();
        Debug.error("processCustomReceiverMessage");
    }

    /**
     * 处理小米通知栏消息点击后的事件
     * 注意:必须要是小米 push && 通知栏消息(silent = false)
     *
     * @param intent
     */
    private void processMiNotificationClickEvent(Context context, Intent intent) {
        Toast.makeText(context, "小米通知栏消息被点击", Toast.LENGTH_SHORT).show();
        Debug.error("processMiNotificationClickEvent");
        CallIn.Companion.show(context);
    }

//public class XiaoMiReceiver extends PushMessageReceiver {
//public class XiaoMiReceiver extends AVMiPushMessageReceiver {

//    @Override
//    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
//
//        Debug.error(miPushMessage.toString());
//        super.onReceivePassThroughMessage(context, miPushMessage);
//    }


    void to(Intent intent) {
        String action = intent.getAction();
        String channel = intent.getExtras().getString("com.avos.avoscloud.Channel");
        //获取消息内容
        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.avos.avoscloud.Data"));
            Debug.error(action + "\n"
                    + channel + "\n"
                    + json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
