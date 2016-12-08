package com.leancloud.im.chatroom.viewholder;

import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.leancloud.im.chatroom.R;
import com.leancloud.im.chatroom.utils.TimeUtils;

import java.text.SimpleDateFormat;

/**
 * Created by wli on 15/8/13.
 * 聊天时居左的文本 holder
 */

public class LiveChatLeftHolder extends AVCommonViewHolder<AVIMMessage>  {

    private TextView time;
    private TextView name;
    private TextView content;

    public LiveChatLeftHolder(View itemLayoutView, OnRecyclerItemClickListener listener, OnRecyclerItemChildClickListener childListener) {
        super(itemLayoutView, listener, childListener);
    }

    @Override
    public void onBindedView(View itemLayoutView) {
        time = (TextView) itemLayoutView.findViewById(R.id.time);
        name = (TextView) itemLayoutView.findViewById(R.id.name);
        content = (TextView) itemLayoutView.findViewById(R.id.content);
        name.setOnClickListener(this);
    }

    @Override
    public void bindData(AVIMMessage message) {
        SimpleDateFormat formate;
        if (TimeUtils.isSameDayOfMillis(message.getTimestamp(), System.currentTimeMillis())) {
            formate = new SimpleDateFormat("HH:mm");
        } else {
            formate = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        }
        String time = formate.format(message.getTimestamp());

        String content = getContext().getString(R.string.unspport_message_type);
        if (message instanceof AVIMTextMessage) {
            content = ((AVIMTextMessage) message).getText();
        }

        this.time.setText(time);
        this.content.setText(content);
        name.setText(String.format("%s：", message.getFrom()));
    }

    @Override
    public void showTimeView(boolean isShow) {
        time.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

}