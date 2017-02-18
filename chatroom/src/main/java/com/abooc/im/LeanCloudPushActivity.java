package com.abooc.im;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.abooc.plugin.about.AboutActivity;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.LogUtil;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;

import java.util.Date;

public class LeanCloudPushActivity extends AppCompatActivity {


    TextView mMessateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leancloud_push);

        mMessateView = (TextView) findViewById(R.id.message);

//        PushService.setDefaultPushCallback(this, LeanCloudPushActivity.class);

        TextView uid = (TextView) findViewById(R.id.uid);


        // 显示的设备的 installationId，用于推送的设备标示
        uid.setText("这个设备的 id: " + AVInstallation.getCurrentInstallation().getInstallationId());
        // 保存 installation 到服务器
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                AVInstallation.getCurrentInstallation().saveInBackground();
            }
        });

        Update.checkForUpdate(this);
    }

    String time() {
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        return " " + date.toString();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    boolean hasSubDev = false;

    public void onSubscribeDev(View view) {
        TextView textView = (TextView) view;
        if (hasSubDev) {
            hasSubDev = false;
            PushService.unsubscribe(this, "dev");
            AVInstallation.getCurrentInstallation().saveInBackground();

            textView.setText("订阅【开发者频道】");
        } else {
            hasSubDev = true;
            // 订阅频道，当该频道消息到来的时候，打开对应的 Activity
            PushService.subscribe(this, "dev", Callback1.class);
            AVInstallation.getCurrentInstallation().saveInBackground();

            textView.setText("取消订阅【开发者频道】");
        }
    }

    boolean hasSubPublic = false;

    public void onSubscribePublic(View view) {
        TextView textView = (TextView) view;
        if (hasSubPublic) {
            hasSubPublic = false;
            PushService.unsubscribe(this, "public");
            AVInstallation.getCurrentInstallation().saveInBackground();

            textView.setText("订阅【公共频道】");
        } else {
            hasSubPublic = true;
            // 订阅频道，当该频道消息到来的时候，打开对应的 Activity
            PushService.subscribe(this, "public", Callback2.class);
            AVInstallation.getCurrentInstallation().saveInBackground();

            textView.setText("取消订阅【公共频道】");
        }
    }

    public void onSubscribeAll(View view) {
        // 订阅频道，当该频道消息到来的时候，打开对应的 Activity
        PushService.subscribe(this, "all", Callback1.class);
        AVInstallation.getCurrentInstallation().saveInBackground();
    }

    boolean hasRegisterReceiver = false;
    MyCustomReceiver mMyCustomReceiver = new MyCustomReceiver();

    public void onCustomReceiver(View view) {
        TextView textView = (TextView) view;
        if (hasRegisterReceiver) {
            hasRegisterReceiver = false;

            unregisterReceiver(mMyCustomReceiver);

            textView.setText("开启【自定义Receiver】");
        } else {
            hasRegisterReceiver = true;

            IntentFilter intentFilter = new IntentFilter("com.abooc.UPDATE");
            intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
            intentFilter.addAction("android.intent.action.USER_PRESENT");
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

            registerReceiver(mMyCustomReceiver, intentFilter);

            textView.setText("关闭【自定义Receiver】");
        }

    }

    public void onAbout(View view) {
        AboutActivity.launch(this);
    }

    class MyCustomReceiver extends BroadcastReceiver {
        private static final String TAG = "Debug";

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.log.d(TAG, "Get Broadcat");
            String action = intent.getAction();
            String channel = intent.getExtras().getString("com.avos.avoscloud.Channel");
            //获取消息内容
            String data = intent.getExtras().getString("com.avos.avoscloud.Data");
            mMessateView.setText("action:" + action + "\nchannel:" + channel + "\ndata:" + data);
        }
    }

}
