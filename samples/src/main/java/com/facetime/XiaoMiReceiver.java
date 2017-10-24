package com.facetime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.abooc.im.message.CallMessage;
import com.abooc.util.Debug;
import com.avos.avoscloud.AVConstants;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.lee.java.util.Empty;

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

    /**
     * 处理自定义消息
     *
     * @param context
     * @param intent
     */
    private void processCustomReceiverMessage(Context context, Intent intent) {
        Debug.error("processCustomReceiverMessage");

//        handleIntent(context, intent);
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

        handleIntent(context, intent);
    }

    public static void handleIntent(Context context, Intent intent) {

        CallMessage message = to(intent);
        if (message != null) {
            int action = message.getC_Action();
            if (action == CallMessage.ACTION_CALL) {
                FaceTimeActivity.Companion.show(context, message.getC_From(), CallMessage.ACTION_CALL);
            } else {
                com.abooc.widget.Toast.show("【" + message.getC_From() + "】未接来电！");
                X.show(context);
            }
        } else {
            X.show(context);
        }
    }

//public class XiaoMiReceiver extends PushMessageReceiver {
//public class XiaoMiReceiver extends AVMiPushMessageReceiver {

//    @Override
//    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
//
//        Debug.error(miPushMessage.toString());
//        super.onReceivePassThroughMessage(context, miPushMessage);
//    }


    public static CallMessage to(Intent intent) {
        String action = intent.getAction();
        String channel = intent.getExtras().getString("com.avos.avoscloud.Channel");
        String data = intent.getExtras().getString("com.avos.avoscloud.Data");
        if (Empty.isEmpty(data)) {
            return null;
        }
        //获取消息内容
        try {
            JSONObject obj = new JSONObject(data);
            Debug.error(action + "\n"
                    + channel + "\n"
                    + obj);

            String json = obj.getString("c_data");
            CallMessage message = new Gson().fromJson(json, CallMessage.class);
            Debug.error("c_action:" + message.getC_Action() + "\n"
                    + "c_title:" + message.getC_Title() + "\n"
                    + "text:" + message.getText() + "\n"
                    + "c_from:" + message.getC_From() + "\n"
                    + "c_to:" + message.getC_To() + "\n"
                    + message.toString());
            return message;
        } catch (JSONException e) {
            Debug.error(e);
        }
        return null;
    }

}
