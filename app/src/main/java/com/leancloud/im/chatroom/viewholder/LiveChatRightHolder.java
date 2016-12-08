package com.leancloud.im.chatroom.viewholder;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.leancloud.im.chatroom.R;
import com.leancloud.im.chatroom.utils.TimeUtils;

import java.text.SimpleDateFormat;

/**
 * Created by wli on 15/8/13.
 * * 聊天时居右的文本 holder
 */
public class LiveChatRightHolder extends AVCommonViewHolder<AVIMMessage> {

    private TextView time;
    private TextView name;
    private TextView content;
    private FrameLayout status;
    private ProgressBar loading;
    private ImageView error;

    public LiveChatRightHolder(View itemLayoutView, OnRecyclerItemClickListener listener, OnRecyclerItemChildClickListener childListener) {
        super(itemLayoutView, listener, childListener);
    }

    @Override
    public void onBindedView(View itemLayoutView) {
        time = (TextView) itemLayoutView.findViewById(R.id.time);
        name = (TextView) itemLayoutView.findViewById(R.id.name);
        content = (TextView) itemLayoutView.findViewById(R.id.content);
        status = (FrameLayout) itemLayoutView.findViewById(R.id.status);
        loading = (ProgressBar) itemLayoutView.findViewById(R.id.progressbar);
        error = (ImageView) itemLayoutView.findViewById(R.id.error);
        error.setOnClickListener(this);
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

        this.content.setText(content);
        this.time.setText(time);
        name.setText(String.format("%s：", message.getFrom()));

        if (AVIMMessage.AVIMMessageStatus.AVIMMessageStatusFailed == message.getMessageStatus()) {
            status.setVisibility(View.VISIBLE);
            error.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
        } else if (AVIMMessage.AVIMMessageStatus.AVIMMessageStatusSending == message.getMessageStatus()) {
            status.setVisibility(View.VISIBLE);
            error.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
        } else {
            status.setVisibility(View.GONE);
        }
    }

    @Override
    public void showTimeView(boolean isShow) {
        time.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

}
