package com.abooc.im;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 */
public class Callback1 extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback1);

        TextView textView = (TextView) findViewById(R.id.uid);

        Intent intent = getIntent();

        String action = intent.getAction();
        String channel = intent.getExtras().getString("com.avos.avoscloud.Channel");
        //获取消息内容
        String dataJson = intent.getExtras().getString("com.avos.avoscloud.Data");

        StringBuffer buffer = new StringBuffer();
        buffer.append("action:").append(action).append("\n")
                .append("channel:").append(channel).append("\n")
                .append("data:").append("\n").append(dataJson);

        textView.setText(buffer.toString());
    }

}