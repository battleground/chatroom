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

import com.abooc.im.AppApplication;
import com.abooc.im.MessageIdentifier;
import com.abooc.im.R;
import com.abooc.im.message.GiftMessage;
import com.abooc.plugin.about.AboutActivity;
import com.abooc.util.Debug;
import com.abooc.widget.Toast;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.google.gson.Gson;

import java.util.Locale;

public class GiftSamplesActivity extends AppCompatActivity {


    int mGoldTotal = 4000;
    TextView mGoldText;

    Gson mGson = new Gson();
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
        setTitle(AppApplication.LC_CLIENT + " - 在线");

        AVIMMessageManager.registerAVIMMessageType(GiftMessage.class);
        AVIMMessageManager.registerMessageHandler(GiftMessage.class, iCustomMessageHandler);

        mGoldText = (TextView) findViewById(R.id.gold);
        mMessageText = (TextView) findViewById(R.id.message);

        mXText = (TextView) findViewById(R.id.X);
        mXText.setVisibility(View.INVISIBLE);
        mAnimationX = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_out);

        mClient = AVIMClient.getInstance(AppApplication.LC_CLIENT);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                AboutActivity.launch(this);
                return true;
            case R.id.logout:
                logout(false);
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

        if (login) {
            logout(true);
        }
    }

    void charge(GiftMessage message) {
        mGoldTotal += message.getMoney();
        mGoldText.setText(String.valueOf(mGoldTotal));
        mGoldText.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
    }

    CustomMessageHandler iCustomMessageHandler = new CustomMessageHandler();
    AVIMClient mClient;

    public void onSendGift(View view) {
        GiftMessage giftMessage = new GiftMessage();
        giftMessage.setText("发送一个礼物！");
        giftMessage.setName("【普通礼物】");
        giftMessage.setCode("GF-01-333");
        giftMessage.setMoney(100);
        giftMessage.setUid(AppApplication.LC_CLIENT);

        if (!check(giftMessage.getMoney())) {
            mGoldText.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            return;
        }

        doSend(giftMessage);
        giftMessage.setMoney(0 - giftMessage.getMoney());
        charge(giftMessage);
    }

    public void onSendBigGift(View view) {
        GiftMessage giftMessage = new GiftMessage();
        giftMessage.setText("发送一个礼物！");
        giftMessage.setName("【豪华礼物】");
        giftMessage.setCode("GF-10-0001");
        giftMessage.setMoney(1000);
        giftMessage.setUid(AppApplication.LC_CLIENT);

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
        AVIMConversation conversation = mClient.getConversation("592fbc5c1b69e6005ca9c156");
        message = iMessageIdentifier.eat(message);

        conversation.sendMessage(message, new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {
                    Toast.show("礼物已发送！");
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
        AVIMMessageManager.unregisterMessageHandler(GiftMessage.class, iCustomMessageHandler);
        mClient.close(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (e == null) {
                    login = false;
                    if (!fromDestroy) {
                        LoginActivity.launch(getBaseContext());
                        finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                } else {
                    Toast.show(" 退出失败！");
                    Debug.error(AppApplication.LC_CLIENT + " 退出失败！" + e);
                }
            }
        });
    }


    public class CustomMessageHandler extends AVIMMessageHandler {
        //接收到消息后的处理逻辑
        @Override
        public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
            if (message instanceof GiftMessage) {
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
                Debug.anchor(mGson.toJson(message));
            }
        }

        public void onMessageReceipt(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {
            Debug.error();
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
            Claim.show(GiftSamplesActivity.this);
        }

        String toTime(long time) {
            java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
            return format1.format(time);
        }
    }


}
