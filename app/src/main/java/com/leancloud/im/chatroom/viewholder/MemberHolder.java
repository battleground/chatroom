package com.leancloud.im.chatroom.viewholder;

import android.view.View;
import android.widget.TextView;

import com.abooc.android.widget.ViewHolder;
import com.leancloud.im.chatroom.R;
import com.leancloud.im.chatroom.adapter.MembersAdapter.MemberItem;

import butterknife.Bind;

/**
 * Created by wli on 15/8/14.
 */
public class MemberHolder extends ViewHolder {

    @Bind(R.id.member_item_name)
    public TextView name;

    public MemberHolder(View itemLayoutView, OnRecyclerItemClickListener listener) {
        super(itemLayoutView, listener);
    }

    @Override
    public void onBindedView(View itemLayoutView) {
        name = (TextView) itemLayoutView.findViewById(R.id.member_item_name);
    }

    public void bindData(MemberItem member) {
        name.setText(member.content);
    }

}