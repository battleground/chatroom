package com.abooc.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.abooc.im.Chat;
import com.abooc.im.LeanCloud;
import com.abooc.im.MVP;
import com.abooc.im.R;
import com.abooc.im.message.FMIMSystemMessage;
import com.abooc.im.message.FMTextMessage;
import com.abooc.im.message.GiftMessage;
import com.abooc.util.Debug;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.google.gson.Gson;

import java.util.Date;
import java.util.List;

public class LeanCloudIMActivity extends AppCompatActivity implements MVP.HomeViewer {


    public static void launch(Context context) {
        Intent intent = new Intent(context, LeanCloudIMActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    TextView mMessageView;
    Chat mChat = new Chat();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leancloud_im);

        mMessageView = (TextView) findViewById(R.id.message);
        mChat.setViewer(this);
    }

    String time() {
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        return " " + date.toString();
    }

    public void onLogin(View view) {
        String clientId = LeanCloud.getInstance().getClientId();
        TextView textView = (TextView) view;
        if (view.getTag() == null) {
            view.setTag(true);

            mChat.login(clientId);

            textView.setText(String.format("退出，【%s已登录】", clientId));


            final String installationId = clientId;
            AVInstallation.getCurrentInstallation().put("uid", installationId);
            AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        Debug.anchor("OK, " + installationId);
                    } else {
                        Debug.error("ERROR, " + installationId + ", " + e);
                    }
                }
            });
        } else {
            view.setTag(null);

            mChat.close();

            textView.setText("登录");

            final String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
            AVInstallation.getCurrentInstallation().put("uid", installationId);
            AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        Debug.anchor("OK, " + installationId);
                    } else {
                        Debug.error("ERROR, " + installationId + ", " + e);
                    }
                }
            });
        }

    }

    public void onJoinConversation(View view) {
        String conversationId = LeanCloud.CONVERSATION_ID;
        TextView textView = (TextView) view;
        if (view.getTag() == null) {
            view.setTag(true);
            mChat.join(conversationId);
            String text = String.format("退出会话，【已加入%s】", conversationId);
            textView.setText(text);
        } else {
            view.setTag(null);
            mChat.quit(conversationId);

            textView.setText(String.format("加入会话【%s】", conversationId));
        }
    }

    public void onJoinConversation2(View view) {
        String conversationId = LeanCloud.CONVERSATION_ID_2;
        TextView textView = (TextView) view;
        if (view.getTag() == null) {
            view.setTag(true);
            mChat.join(conversationId);
            String text = String.format("退出会话 2，【已加入%s】", conversationId);
            textView.setText(text);
        } else {
            view.setTag(null);
            mChat.quit(conversationId);

            textView.setText(String.format("加入会话 2【%s】", conversationId));
        }
    }


    public void onLoadHistory(View view) {
        mChat.history();
    }

    public void onStartReceived(View view) {
        TextView textView = (TextView) view;
        if (view.getTag() == null) {
            view.setTag(true);

            mChat.doReceive();
            textView.setText("停止接收消息");
        } else {
            view.setTag(null);
            mChat.cancelReceive();

            textView.setText("接收新消息");
        }
    }

    public void onSendDefaultMessage(View view) {
        AVIMTextMessage textMessage = new AVIMTextMessage();
        textMessage.setText("来自【Tom】的默认消息" + time());

        mChat.send(textMessage);
    }

    public void onSendCustomMessage(View view) {
        FMTextMessage textMessage = new FMTextMessage();
        textMessage.setUid("uid:tom-01");
        textMessage.setUsername("Tom");
        textMessage.setText("来自【Tom】的消息" + time());
        textMessage.setAvatar("http://www.avatar.com/01.png");

        mChat.send(textMessage);
    }

    public void onSendSystemMessage(View view) {
        FMIMSystemMessage systemMessage = new FMIMSystemMessage();
        systemMessage.setAction(11);
        systemMessage.setText("来自【系统】的消息" + time());
        mChat.send(systemMessage);
    }

    public void onSendGiftsMessage(View view) {
        switch (view.getId()) {
            case R.id.gifts_01:
                Gift gift1 = new Gift();
                gift1.code = "101";
                gift1.name = "鲜花";
                sendGiftsMessage(gift1);
                break;
            case R.id.gifts_02:
                Gift gift2 = new Gift();
                gift2.code = "102";
                gift2.name = "掌声";
                sendGiftsMessage(gift2);
                break;
        }
    }

    private void sendGiftsMessage(Gift gift) {
        GiftMessage textMessage = new GiftMessage();
        textMessage.setUid("uid:tom-01");
        textMessage.setUsername("Tom");
        textMessage.setText("来自【Tom】的消息" + time());
        textMessage.setAvatar("http://www.avatar.com/01.png");

        textMessage.setCode(gift.code);
        textMessage.setName(gift.name);

        mChat.send(textMessage);
    }

    class Gift {
        String code;
        String name;
    }

    Gson mGson = new Gson();

    @Override
    public void showList(List<AVIMMessage> list) {
        if (list == null || list.size() == 0) return;
        AVIMMessage message = list.get(list.size() - 1);

        String toJson = mGson.toJson(message);
        mMessageView.setText(toJson);
    }

    @Override
    public void showMessage(AVIMMessage message) {
        String toJson = mGson.toJson(message);
        mMessageView.setText(toJson);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mChat.close();
    }

}
