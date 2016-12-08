package com.leancloud.im.chatroom.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.abooc.util.Debug;
import com.leancloud.im.chatroom.Constants;
import com.leancloud.im.chatroom.R;

/**
 * Created by wli on 15/8/13.
 * 聊天室页面，即群组聊天页面
 * <p>
 * 1、根据 clientId 获得 AVIMClient 实例
 * 2、根据 mConversationId 获得 AVIMConversation 实例
 * 3、必须要加入 conversation 后才能拉取消息
 */
public class AVSquareActivity extends AVBaseActivity {

    private Toolbar toolbar;

    public static void launch(Activity activity, String conversationId, String name) {
        Intent intent = new Intent(activity, AVSquareActivity.class);
        intent.putExtra(Constants.CONVERSATION_ID, conversationId);
        intent.putExtra(Constants.ACTIVITY_TITLE, name);
        activity.startActivityForResult(intent, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square);

        String title = getIntent().getStringExtra(Constants.ACTIVITY_TITLE);
        Debug.error(title);
        attachActionBar(title);
    }

    private void attachActionBar(String title) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        mToolbar.setNavigationIcon(R.drawable.btn_navigation_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setTitle(title);
        Debug.anchor(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_square, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.menu_square_members) {
            String conversationId = getIntent().getStringExtra(Constants.CONVERSATION_ID);
            AVSquareMembersActivity.launch(this, conversationId);
        }
        return super.onOptionsItemSelected(item);
    }

}
