package com.qiniu.pili.droid.streaming;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.abooc.util.Debug;
import com.abooc.widget.Toast;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.leancloud.im.chatroom.AVIMClientManager;
import com.leancloud.im.chatroom.ConversationEventHandler;
import com.leancloud.im.chatroom.R;
import com.leancloud.im.chatroom.activity.AVBaseActivity;

/**
 * Created by wli on 15/8/13.
 * 登陆页面，暂时未做自动登陆，每次重新进入都要再登陆一次
 */
public class AVLoginActivity extends AVBaseActivity implements OnClickListener {

    /**
     * 此处 xml 里限制了长度为 30，汉字算一个
     */
    protected EditText userNameView;
    protected Button loginButton;
    protected TextView logText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameView = (EditText) findViewById(R.id.activity_login_et_username);
        logText = (TextView) findViewById(R.id.activity_login_text_log);
        loginButton = (Button) findViewById(R.id.activity_login_btn_login);
        loginButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.activity_login_btn_login) {
            openClient(userNameView.getText().toString().trim());
        }
    }

    private void openClient(String selfId) {
        if (TextUtils.isEmpty(selfId)) {
            showToast(R.string.login_null_name_tip);
            return;
        }

        loginButton.setEnabled(false);
        userNameView.setEnabled(false);
        AVIMClientManager.getInstance().open(selfId, new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                loginButton.setEnabled(true);
                userNameView.setEnabled(true);
                if (Debug.printStackTrace(e)) {
                    Toast.show("登录失败");
                    return;
                }
                AVIMMessageManager.setConversationEventHandler(new ConversationEventHandler());
//                ChatRoomsActivity.launch(AVLoginActivity.this);
                startActivity(MainActivity.class);
                finish();
            }
        });
    }

}
