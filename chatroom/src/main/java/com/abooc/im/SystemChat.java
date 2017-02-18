package com.abooc.im;

/**
 * Created by dayu on 2017/1/10.
 */

public class SystemChat {

    ChatPresenter chatPresenter = new ChatPresenter();
    void a() {

        FLIMSystemMessage systemMessage = new FLIMSystemMessage();
        systemMessage.setText("来自系统消息！");

        chatPresenter.send(systemMessage);
    }
}
