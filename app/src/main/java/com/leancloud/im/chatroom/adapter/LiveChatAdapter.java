package com.leancloud.im.chatroom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abooc.joker.adapter.recyclerview.BaseRecyclerAdapter;
import com.abooc.joker.adapter.recyclerview.ViewHolder;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.leancloud.im.chatroom.AVIMClientManager;
import com.leancloud.im.chatroom.R;
import com.leancloud.im.chatroom.viewholder.AVCommonViewHolder;
import com.leancloud.im.chatroom.viewholder.ChatStatusHolder;
import com.leancloud.im.chatroom.viewholder.LiveChatLeftHolder;
import com.leancloud.im.chatroom.viewholder.LiveChatRightHolder;

/**
 * Created by wli on 15/8/13.
 * 聊天的 Adapter，此处还有可优化的地方，稍后考虑一下提取出公共的 adapter
 */
public class LiveChatAdapter extends BaseRecyclerAdapter<AVIMMessage> {

    private final int ITEM_LEFT_TEXT = 0;
    private final int ITEM_RIGHT_TEXT = 1;
    private final int ITEM_STATUS = 2;

    // 时间间隔最小为十分钟
    private final long TIME_INTERVAL = 10 * 60 * 1000;

    public LiveChatAdapter(Context context) {
        super(context);
    }

    public AVIMMessage getFirstMessage() {
        if (getCollection().isEmpty()) return null;
        return getCollection().get(0);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_LEFT_TEXT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_chat_text_left, parent, false);
            return new LiveChatLeftHolder(view, mListener, mChildListener);
        } else if (viewType == ITEM_RIGHT_TEXT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_chat_text_right, parent, false);
            return new LiveChatRightHolder(view, null, mChildListener);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_status, parent, false);
            return new ChatStatusHolder(view, null, null);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int position) {
        AVCommonViewHolder holder = (AVCommonViewHolder) h;
        AVIMMessage message = getItem(position);
        if (message == null) return;
        holder.bindData(message);
        holder.showTimeView(shouldShowTime(position));
//        holder.showTimeView(false);
    }

    @Override
    public int getItemViewType(int position) {
        AVIMMessage message = getItem(position);
        if (message.getFrom() == null) {
            return ITEM_STATUS;
        } else if (message.getFrom().equals(AVIMClientManager.getInstance().getClientId())) {
            return ITEM_RIGHT_TEXT;
        } else {
            return ITEM_LEFT_TEXT;
        }
    }

    private boolean shouldShowTime(int position) {
        if (position == 0) {
            return true;
        }
        long lastTime = getItem(position - 1).getTimestamp();
        long curTime = getItem(position).getTimestamp();
        return curTime - lastTime > TIME_INTERVAL;
    }
}