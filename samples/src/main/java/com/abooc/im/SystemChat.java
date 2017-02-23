package com.abooc.im;

import com.abooc.im.message.FMIMSystemMessage;

/**
 * Created by dayu on 2017/1/10.
 */

public class SystemChat {

    Chat chat = new Chat();
    void a() {

        FMIMSystemMessage systemMessage = new FMIMSystemMessage();
        systemMessage.setText("来自系统消息！");

        chat.send(systemMessage);
    }
}
