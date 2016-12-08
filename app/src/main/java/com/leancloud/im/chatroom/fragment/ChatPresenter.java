package com.leancloud.im.chatroom.fragment;

import android.text.TextUtils;

import com.abooc.util.Debug;
import com.abooc.widget.Toast;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.leancloud.im.chatroom.AVIMClientManager;

import java.util.Collections;
import java.util.List;

/**
 * @author zhangjunpu
 * @date 2016/12/8
 */

public class ChatPresenter {

    private IChatFun mChat;
    protected AVIMConversation mConversation;


    public ChatPresenter(IChatFun chat) {
        mChat = chat;
    }

    /**
     * 根据 mConversationId 查取本地缓存中的 conversation，如若没有缓存，则返回一个新建的 conversaiton
     */
    public void getSquare(String conversationId) {
        if (TextUtils.isEmpty(conversationId)) {
            Toast.show("ConversationId不能为空");
            return;
        }

        AVIMClient client = AVIMClientManager.getInstance().getClient();
        if (null != client) {
            mConversation = client.getConversation(conversationId);
        } else {
            Toast.show("请先登录");
            mChat.close(false);
        }
    }

    /**
     * 先查询自己是否已经在该 conversation，如果存在则直接给 chatFragment 赋值，否则先加入，再赋值
     */
    public void queryInSquare(String conversationId) {
        AVIMClient client = AVIMClientManager.getInstance().getClient();
        AVIMConversationQuery conversationQuery = client.getQuery();
        conversationQuery.whereEqualTo("objectId", conversationId);
        conversationQuery.containsMembers(Collections.singletonList(AVIMClientManager.getInstance().getClientId()));
        conversationQuery.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(List<AVIMConversation> list, AVIMException e) {
                if (Debug.printStackTrace(e)) return;
                if (null != list && list.size() > 0) {
                    mChat.setConversation(list.get(0));
                } else {
                    joinSquare();
                }
            }
        });
    }

    /**
     * 加入 conversation
     */
    private void joinSquare() {
        mConversation.join(new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (Debug.printStackTrace(e)) return;
                mChat.setConversation(mConversation);
            }
        });
    }

    /**
     * 退出会话
     */
    public void quit() {
        if (mConversation != null) {
            mConversation.quit(new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    if (Debug.printStackTrace(e)) {
                        Toast.show("未退出会话！");
                        return;
                    }
                    //退出成功
                    mChat.close(true);
                }
            });
        }
    }
}
