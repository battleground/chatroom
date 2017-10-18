package com.abooc.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.abooc.im.LcConfig;
import com.abooc.im.LeanCloud;
import com.abooc.im.MessageIdentifier;
import com.abooc.im.R;
import com.abooc.im.message.CallMessage;
import com.abooc.im.message.FMIMSystemMessage;
import com.abooc.im.message.GiftMessage;
import com.abooc.plugin.about.AboutActivity;
import com.abooc.util.Debug;
import com.abooc.widget.Toast;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMClientEventHandler;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.google.gson.Gson;

import java.util.List;
import java.util.Locale;

public class GiftSamplesActivity extends AppCompatActivity {


    int mGoldTotal = 9000;
    TextView mGoldText;

    TextView mTimerText;
    TextView mMessageText;
    TextView mXText;
    Animation mAnimationX;

    MessageIdentifier iMessageIdentifier = new MessageIdentifier();

    boolean login = true;

    public static void launch(Context context) {
        Intent intent = new Intent(context, GiftSamplesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_samples);
        String clientId = LeanCloud.getInstance().getClientId();
        setTitle(clientId + " - 在线");
        getSupportActionBar().setSubtitle("平台：" + LeanCloud.PLATFORM);

        AVIMMessageManager.registerAVIMMessageType(FMIMSystemMessage.class);
        AVIMMessageManager.registerMessageHandler(FMIMSystemMessage.class, iNotificationMessageHandler);

        mGoldText = (TextView) findViewById(R.id.gold);
        mMessageText = (TextView) findViewById(R.id.message);

        mXText = (TextView) findViewById(R.id.X);
        mXText.setVisibility(View.INVISIBLE);
        mAnimationX = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_out);

        mClient = AVIMClient.getInstance(clientId);

        mTimerText = (TextView) findViewById(R.id.timer);
        iMessageIdentifier.setTimerListener(new MessageIdentifier.SimpleOnTimer() {
            @Override
            public void onStart() {
                mTimerText.setText("倒计时");
            }

            @Override
            public void onTick(long millisUntilFinished) {

                mTimerText.setText("倒计时（" + ((millisUntilFinished + 900) / 1000) + "s）");
            }

            @Override
            public void onFinish() {
                mTimerText.setText("计时结束");
            }

            @Override
            public void onCancelled() {
                mTimerText.setText("停止计时");
            }
        });

        LeanCloud.getInstance().addAVIMClientEventHandler(iAVIMClientEventHandler);


        final AVIMConversation conversation = mClient.getConversation(LcConfig.CONVERSATION_ID_TEMP);
        conversation.queryMessages(20, new AVIMMessagesQueryCallback() {
            @Override
            public void done(List<AVIMMessage> list, AVIMException e) {
                if (e == null) {
                    Debug.anchor("【拉取会话历史】 成功！，未读消息数：" + conversation.getUnreadMessagesCount());
                } else {
                    Debug.error("【拉取会话历史】失败：" + e);
                }
            }
        });

    }

    private AVIMClientEventHandler iAVIMClientEventHandler = new AVIMClientEventHandler() {
        @Override
        public void onConnectionPaused(AVIMClient avimClient) {
            setTitle(avimClient.getClientId() + " - 离线");
        }

        @Override
        public void onConnectionResume(AVIMClient avimClient) {
            setTitle(avimClient.getClientId() + " - 在线");
        }

        @Override
        public void onClientOffline(AVIMClient avimClient, int i) {
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean online = LeanCloud.getInstance().isOnline();
        MenuItem item = menu.findItem(R.id.online);
        item.setTitle(online ? "离线" : "上线");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.online:
                LeanCloud leanCloud = LeanCloud.getInstance();
                leanCloud.online(!leanCloud.isOnline());

                String clientId = LeanCloud.getInstance().getClientId();
                setTitle(clientId + (!leanCloud.isOnline() ? " - 在线" : " - 离线"));
                return true;
            case R.id.menu_logout:
                logout(false);
                return true;
            case R.id.menu_about:
                AboutActivity.launch(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onStopTimer(View view) {
        iMessageIdentifier.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LeanCloud.getInstance().removeAVIMClientEventHandler(iAVIMClientEventHandler);
    }

    void charge(GiftMessage message) {
        mGoldTotal += message.getMoney();
        mGoldText.setText(String.valueOf(mGoldTotal));
        mGoldText.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
    }

    NotificationMessageHandler iNotificationMessageHandler = new NotificationMessageHandler();
    AVIMClient mClient;

    public void onSendGift(View view) {
        String clientId = LeanCloud.getInstance().getClientId();
        GiftMessage giftMessage = new GiftMessage();
        giftMessage.setText("发送一个礼物！");
        giftMessage.setName("【普通礼物】");
        giftMessage.setCode("GF-01-333");
        giftMessage.setMoney(100);
        giftMessage.setUid(clientId);

        if (!check(giftMessage.getMoney())) {
            mGoldText.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            return;
        }

        doSend(giftMessage);
        giftMessage.setMoney(0 - giftMessage.getMoney());
        charge(giftMessage);
    }

    public void onSendBigGift(View view) {
        String clientId = LeanCloud.getInstance().getClientId();
        GiftMessage giftMessage = new GiftMessage();
        giftMessage.setText("发送一个礼物！");
        giftMessage.setName("【豪华礼物】");
        giftMessage.setCode("GF-10-0001");
        giftMessage.setMoney(1000);
        giftMessage.setUid(clientId);

        if (!check(giftMessage.getMoney())) {
            mGoldText.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            return;
        }

        doSend(giftMessage);
        giftMessage.setMoney(0 - giftMessage.getMoney());
        charge(giftMessage);
    }

    boolean check(int i) {
        if (mGoldTotal < i) {
            return false;
        }
        return true;
    }

    void doSend(GiftMessage message) {
        AVIMConversation conversation = mClient.getConversation(LcConfig.CONVERSATION_ID_TEMP);
        message = iMessageIdentifier.eat(message);

        conversation.sendMessage(message, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {
                    Toast.show("礼物已发送！");
                    iMessageIdentifier.start();
                } else {
                    Debug.error("礼物发送失败！" + e);
                }
            }
        });
    }

    /**
     * 注销账号
     */
    private void logout(final boolean fromDestroy) {
        LeanCloud.getInstance().online(false);

        AVIMMessageManager.unregisterMessageHandler(FMIMSystemMessage.class, iNotificationMessageHandler);
//        AVIMMessageManager.unregisterMessageHandler(GiftMessage.class, iCustomMessageHandler);
        mClient.close(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (e == null) {
                    login = false;
                } else {
                    String clientId = mClient.getClientId();
                    Debug.error(clientId + " 退出失败！" + e);
                }
                if (!fromDestroy) {
                    exitPage();
                }
            }
        });
    }

    private void exitPage() {
        LoginActivity.launch(getBaseContext());
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    public class NotificationMessageHandler extends AVIMMessageHandler {
        //接收到消息后的处理逻辑
        @Override
        public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
            if (message instanceof FMIMSystemMessage) {
                FMIMSystemMessage systemMessage = (FMIMSystemMessage) message;
                Toast.show(systemMessage.getText());

                logout(false);

            } else {
                Debug.anchor(new Gson().toJson(message));
            }
        }
    }

    public class CustomMessageHandler extends AVIMMessageHandler {
        //接收到消息后的处理逻辑
        @Override
        public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
            if (message instanceof CallMessage) {
                GiftMessage giftMessage = (GiftMessage) message;

                addToContainer(giftMessage);
                charge(giftMessage);

                switch (giftMessage.getMoney()) {
                    case 100:
                        doGift(giftMessage);
                        break;
                    case 1000:
                        doBigGift(giftMessage);
                        break;
                }
            } else {
                Debug.anchor(new Gson().toJson(message));
            }
        }

        public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
            Debug.error();
        }

    }

    void addToContainer(GiftMessage giftMessage) {
        int index = giftMessage.getGiftIndex();
        String giftIndex = index > 1 ? " X " + index : "";
        String messageString = "【" + giftMessage.getUid() + "】：" + giftMessage.getName() + giftIndex;
        CharSequence text = mMessageText.getText();
        String time = toTime(giftMessage.getTimestamp());
        mMessageText.setText(text + "\n" + time + messageString);
    }

    void doGift(GiftMessage giftMessage) {
        if (giftMessage.getGiftIndex() > 1) {
            mXText.startAnimation(mAnimationX);
            mXText.setText("X " + giftMessage.getGiftIndex());
        } else {
            mXText.setText(null);
        }
    }

    void doBigGift(GiftMessage giftMessage) {
        Claim.show(this);
    }

    String toTime(long time) {
        java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        return format1.format(time);
    }

}
