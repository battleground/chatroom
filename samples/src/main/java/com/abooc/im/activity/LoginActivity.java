package com.abooc.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.abooc.im.LeanCloud;
import com.abooc.im.R;
import com.abooc.im.unittest.DebugListActivity;
import com.abooc.util.Debug;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMClientOpenOption;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;

public class LoginActivity extends AppCompatActivity {


    private TextView mMessageText;
    private boolean logining;
    private String clientId;


    public static final String LEANCOUND_CLIENT_TOM = "Tom";
    public static final String LEANCOUND_CLIENT_JERRY = "Jerry";


    public static void launch(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mMessageText = (TextView) findViewById(R.id.log);

        RadioGroup iRadioGroup = (RadioGroup) findViewById(R.id.RadioGroup);
        RadioButton radioButton = (RadioButton) iRadioGroup.getChildAt(0);
        iRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                View checkedView = group.findViewById(checkedId);
                if (group.indexOfChild(checkedView) == 0) {
                    clientId = LEANCOUND_CLIENT_TOM;
                } else {
                    clientId = LEANCOUND_CLIENT_JERRY;
                }
            }
        });
        radioButton.toggle();

    }

    public void onLogin(final View view) {
        mMessageText.setText("loading...");
        mMessageText.setVisibility(View.VISIBLE);
        logining = true;
        view.setEnabled(false);
        AVIMClient avimClient = LeanCloud.getInstance().createClient(clientId);
        AVIMClientOpenOption openOption = new AVIMClientOpenOption();
        openOption.setForceSingleLogin(true);
        avimClient.open(openOption, new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (e == null) {
                    Debug.anchor(clientId + "【登录】成功");
                    mMessageText.setText("登录成功");
                    mMessageText.setVisibility(View.VISIBLE);

                    LeanCloud.getInstance().online(true);

                    AVIMConversation conversation = avimClient.getConversation(LeanCloud.CONVERSATION_ID_TOM_JERRY);
                    conversation.join(new AVIMConversationCallback() {
                        @Override
                        public void done(AVIMException e) {
                            if (e == null) {
                                Debug.anchor("加入 【Tom & Jerry】 会话，成功！");
                            } else {
                                Debug.error("加入【Tom & Jerry】 会话，失败：" + e);
                            }
                        }
                    });

                    final AVIMConversation c = avimClient.getConversation(LeanCloud.CONVERSATION_ID_TOM_JERRY_SYSTEM);
                    c.join(new AVIMConversationCallback() {
                        @Override
                        public void done(AVIMException e) {
                            if (e == null) {
                                Debug.anchor("加入 【Tom系统】 会话，成功！");
                            } else {
                                Debug.error("加入【Tom系统】 会话，失败：" + e);
                            }
                        }
                    });


                    new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            GiftSamplesActivity.launch(getBaseContext());
                            finish();
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    }.sendEmptyMessageDelayed(0, 1000);
                } else {
                    Debug.error(clientId + "【登录】失败, " + e);
                    view.setEnabled(true);
                    mMessageText.setText("登录失败");
                    mMessageText.setVisibility(View.VISIBLE);

                    new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            logining = false;
                            mMessageText.setVisibility(View.INVISIBLE);
                        }
                    }.sendEmptyMessageDelayed(0, 2000);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!logining)
            super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void onOpenDebug(View view) {
        DebugListActivity.launch(this);
    }

}
