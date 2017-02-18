package com.abooc.im;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.google.gson.Gson;

import java.util.Date;
import java.util.List;

public class LeanCloudIMActivity extends Activity implements MVP.HomeViewer {


    TextView mMessateView;
    ChatPresenter mChatPresenter = new ChatPresenter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leancloud_im);

        mMessateView = (TextView) findViewById(R.id.message);
        mChatPresenter.setViewer(this);
    }

    String time() {
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        return " " + date.toString();
    }

    public void onLogin(View view) {
        mChatPresenter.login();
    }

    public void onJoinConversation(View view) {
        mChatPresenter.join();
    }

    public void onQuitConversation(View view) {
        mChatPresenter.quit();
    }

    public void onLoadHistory(View view) {
        mChatPresenter.history();
    }

    public void onSendDefaultMessage(View view) {
        AVIMTextMessage textMessage = new AVIMTextMessage();
        textMessage.setText("来自【Tom】的默认消息" + time());

        mChatPresenter.send(textMessage);
    }

    public void onSend(View view) {
        FMTextMessage textMessage = new FMTextMessage();
        textMessage.setUid("uid:tom-01");
        textMessage.setUsername("Tom");
        textMessage.setText("来自【Tom】的消息" + time());
        textMessage.setAvatar("http://www.avatar.com/01.png");

        mChatPresenter.send(textMessage);
    }

    public void onSendSystemMessage(View view) {
        FLIMSystemMessage systemMessage = new FLIMSystemMessage();
        systemMessage.setAction(11);
        systemMessage.setText("来自【系统】的消息" + time());
        mChatPresenter.send(systemMessage);
    }

    Gson mGson = new Gson();

    @Override
    public void showList(List<AVIMMessage> list) {
        AVIMMessage message = list.get(list.size() - 1);

        String toJson = mGson.toJson(message);
        mMessateView.setText(toJson);
    }

    @Override
    public void showMessage(AVIMMessage message) {
        String toJson = mGson.toJson(message);
        mMessateView.setText(toJson);

    }

    public void onStartReceived(View view) {
        mChatPresenter.doReceive();
    }

    public void onStopReceived(View view) {
        mChatPresenter.cancelReceive();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mChatPresenter.close();
    }

}
