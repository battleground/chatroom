package com.leancloud.im.chatroom.fragment;

import com.avos.avoscloud.im.v2.AVIMConversation;

/**
 * @author zhangjunpu
 * @date 2016/12/8
 */

public interface IChatFun {
    void setConversation(AVIMConversation conversation);
    void close(boolean isResult);
}
