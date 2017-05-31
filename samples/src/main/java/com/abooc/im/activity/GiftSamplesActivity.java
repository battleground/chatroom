package com.abooc.im.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.abooc.im.MessageIdentifier;
import com.abooc.im.R;
import com.abooc.im.message.GiftMessage;
import com.abooc.util.Debug;
import com.abooc.widget.Toast;
import com.google.gson.Gson;

public class GiftSamplesActivity extends AppCompatActivity {


    Gson mGson = new Gson();
    TextView mTimerText;

    MessageIdentifier iMessageIdentifier = new MessageIdentifier();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_samples);
        mTimerText = (TextView) findViewById(R.id.timer);
        iMessageIdentifier.setTimerListener(new MessageIdentifier.OnSamplesTimer() {
            @Override
            public void onStart() {
                mTimerText.setText("倒计时");

            }

            @Override
            public void onTick(long millisUntilFinished) {

                mTimerText.setText("倒计时（" + (millisUntilFinished / 1000) + "s）");
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

    public void onSendGift_01(View view) {
        GiftMessage giftMessage = new GiftMessage();
        giftMessage = iMessageIdentifier.eat(giftMessage);

        if (giftMessage.getGiftIndex() > 1) {

            Toast.show("礼物：X " + giftMessage.getGiftIndex());
        } else {
            Toast.show("一个礼物：" + giftMessage.getGiftIndex());
        }

        Debug.anchor(mGson.toJson(giftMessage));
    }


    public void onStopTimer(View view) {
        iMessageIdentifier.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
