package com.leancloud.im.chatroom.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.abooc.chatroom.BlankActivity;
import com.leancloud.im.chatroom.AVIMClientManager;
import com.leancloud.im.chatroom.R;
import com.leancloud.im.chatroom.fragment.ChatRoomListFragment;

/**
 * Created by dayu on 2016/12/5.
 */

public class ChatRoomsActivity extends BlankActivity {

    public static void launch(Context ctx) {
        Intent intent = new Intent(ctx, ChatRoomsActivity.class);
//        intent.putExtra(Constants.MEMBER_ID, memberId);
        ctx.startActivity(intent);
    }

//    String memberId;
    ChatRoomListFragment fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        memberId = getIntent().getStringExtra(Constants.MEMBER_ID);
        String memberId = AVIMClientManager.getInstance().getClientId();
        getSupportActionBar().setSubtitle(memberId + " - " + "在线");

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragment = new ChatRoomListFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString(Constants.MEMBER_ID, memberId);
//        fragment.setArguments(bundle);
        transaction.add(R.id.FrameLayout, fragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            fragment.queryConversations();
        }
    }
}
