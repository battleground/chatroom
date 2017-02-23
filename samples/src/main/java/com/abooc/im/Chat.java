package com.abooc.im;

import com.abooc.im.message.FMIMSystemMessage;
import com.abooc.util.Debug;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import org.lee.java.util.ToString;

import java.util.List;

/**
 * Created by dayu on 2017/1/10.
 */

public class Chat {

    AVIMClient mClient;

    MVP.HomeViewer mHomeViewer;

    void setViewer(MVP.HomeViewer viewer) {
        mHomeViewer = viewer;
    }


    void doReceive() {

//      或  AVIMMessageManager.registerMessageHandler(AVIMTextMessage.class, iAVIMMessageHandler);
        AVIMMessageManager.registerDefaultMessageHandler(iAVIMMessageHandler);

        AVIMMessageManager.registerAVIMMessageType(FMIMSystemMessage.class);
        AVIMMessageManager.registerMessageHandler(FMIMSystemMessage.class, iFLIMSystemMessageHandler);
    }

    void cancelReceive() {
        AVIMMessageManager.unregisterMessageHandler(AVIMTextMessage.class, iAVIMMessageHandler);
        AVIMMessageManager.unregisterMessageHandler(AVIMTextMessage.class, iFLIMSystemMessageHandler);
    }

    AVIMMessageHandler iAVIMMessageHandler = new AVIMMessageHandler() {

        public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
            Debug.anchor(message);
            mHomeViewer.showMessage(message);
        }

        public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
            Debug.error(message);
        }
    };

    FLIMSystemMessageHandler iFLIMSystemMessageHandler = new FLIMSystemMessageHandler();

    class FLIMSystemMessageHandler extends AVIMTypedMessageHandler<FMIMSystemMessage> {
        @Override
        public void onMessage(FMIMSystemMessage message, AVIMConversation avimConversation, AVIMClient avimClient) {
            Debug.anchor("Action:" + message.getAction() + ", " + message);
            mHomeViewer.showMessage(message);
        }

        @Override
        public void onMessageReceipt(FMIMSystemMessage message, AVIMConversation avimConversation, AVIMClient avimClient) {
            Debug.anchor("Action:" + message.getAction() + ", " + message);
            Debug.error(message);

        }
    }


    void login(String clientId) {
        mClient = AVIMClient.getInstance(clientId);
        mClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e == null) {
                    Debug.anchor("登录：成功");

                } else
                    Debug.anchor("登录：" + e);
            }
        });
    }

    AVIMConversation mConversation;

    void join(String conversationID) {
        mConversation = mClient.getConversation(conversationID);
        mConversation.join(new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {
                    Debug.anchor("加入会话：成功");

                } else
                    Debug.anchor("加入会话：" + e);
            }
        });
    }

    void quit() {
        mConversation.quit(new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {
                    Debug.anchor("退出会话：成功");
                } else
                    Debug.anchor("退出会话：" + e);
            }
        });
    }

    void send(AVIMMessage message) {
        mConversation.sendMessage(message, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {
                    Debug.anchor("Tom发送消息：成功");

                } else
                    Debug.anchor("Tom发送消息：" + e);
            }
        });
    }

    void history() {
        mConversation.queryMessages(new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> list, AVIMException e) {
                if (e == null) {
                    Debug.anchor("Tom读取历史消息：成功");

                    String toString = ToString.toString(list);
                    Debug.anchor("共 " + list.size() + " 条消息：\n" + toString);

                    mHomeViewer.showList(list);
                } else {
                    Debug.anchor("Tom读取历史消息：" + e);
                }
            }
        });
    }

    void close() {
        mClient.close(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (e == null) {
                    Debug.anchor("Tom退出登录：成功");
                } else
                    Debug.anchor("Tom退出登录：" + e);
            }
        });
    }
}
