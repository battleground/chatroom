package com.abooc.im;

import android.app.Activity;
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
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.leancloud.im.chatroom.AVIMClientManager;
import com.leancloud.im.chatroom.ConversationEventHandler;
import com.leancloud.im.chatroom.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wli on 15/8/13.
 * 登陆页面，暂时未做自动登陆，每次重新进入都要再登陆一次
 */
public class LoginActivity extends Activity implements OnClickListener {

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


        AVIMClient tom = AVIMClient.getInstance("Tom");
        tom.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e == null) {
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        String uid = userNameView.getText().toString().trim();
        if (!TextUtils.isEmpty(uid)) {
            openClient(uid);
        } else {
            Toast.show(R.string.login_null_name_tip);
        }
    }

    private void openClient(String selfId) {
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
                MainActivity.launch(LoginActivity.this);
//                MainActivity.launch(LoginActivity.this);
                finish();
            }
        });
    }

    void send(){
        AVIMClient tom = AVIMClient.getInstance("Tom");
        AVIMTextMessage msg = new AVIMTextMessage();
        msg.setText("来自 Tom 的系统消息");
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("location", "拉萨布达拉宫");
        attributes.put("Title", "这蓝天……我彻底是醉了");
        msg.setAttrs(attributes);
        tom.getConversation("5836b565da2f600062854730").sendMessage(msg,
                new AVIMConversationCallback() {
                    @Override
                    public void done(AVIMException e) {
                        if (e == null) {
                            // 发送成功
                        }
                    }
                });
    }

}
