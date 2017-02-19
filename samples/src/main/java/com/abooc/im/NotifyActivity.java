package com.abooc.im;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.abooc.util.Debug;
import com.google.gson.Gson;

/**
 * 通知跳转分发器
 */
public class NotifyActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        String action = intent.getAction();
        String channel = intent.getExtras().getString("com.avos.avoscloud.Channel");
        //获取消息内容
        String dataJson = intent.getExtras().getString("com.avos.avoscloud.Data");

        StringBuffer buffer = new StringBuffer();
        buffer.append("ACTION:").append(action).append("\n")
                .append("CHANNEL:").append(channel).append("\n")
                .append("DATA:").append("\n").append(dataJson);


        Gson gson = new Gson();
        NotifyMessage notifyMessage = gson.fromJson(dataJson, NotifyMessage.class);
        if (notifyMessage == null
                || notifyMessage.payload == null
                || notifyMessage.payload.model == null) {
            onBackPressed();
            return;
        }
        String type = notifyMessage.payload.type;
        switch (type) {
            case "1":
                start(Callback1.class);
                onBackPressed();
                break;
            case "2":
                start(Callback2.class);
                onBackPressed();
                break;
            case "3":
                start(LeanCloudIMActivity.class);
                onBackPressed();
                break;
            default:
                onBackPressed();
                break;
        }

        Debug.anchor("type:" + type + ", " + notifyMessage.payload.model.toString());
    }

    void start(Class<? extends Activity> cls) {
        Bundle extras = getIntent().getExtras();

        Intent intent = new Intent(this, cls);
        intent.putExtras(extras);
        startActivity(intent);

    }

}