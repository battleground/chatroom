package com.abooc.im.unittest;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abooc.im.R;
import com.abooc.im.activity.Claim;
import com.abooc.util.Debug;

public class TestAnimActivity extends AppCompatActivity {


    public static void launch(Context context) {
        Intent intent = new Intent(context, TestAnimActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private SoundPool mSoundPool;
    private int mSoundID;
    private float mVolume = 1.0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_anim);

        mXText = (TextView) findViewById(R.id.X);
        mXText.setVisibility(View.INVISIBLE);
        mAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_out);

        mSoundPool = new SoundPool(5, AudioManager.STREAM_RING, 0);
        mSoundID = mSoundPool.load(getApplicationContext(), R.raw.diaoluo_da, 1);
    }

    Animation mAnimation;
    TextView mXText;

    public void onAnim(View view) {
        int index = (int) (Math.random() * 10);
        mXText.setText("X " + index);
        mXText.startAnimation(mAnimation);

    }

    public void onPlaySoundUp(View view) {
        mVolume += 0.2;
        playSound();
    }

    public void onPlaySoundDown(View view) {
        mVolume -= 0.2;
        playSound();
    }

    int mGoldTotal = 4000;

    public void onChargeGold(View view) {
        if (mGoldTotal > 0) {
            mGoldTotal -= ((int) (Math.random() * 10) * 100);

            String money = String.valueOf(mGoldTotal);

            TextView textView = (TextView) view;
            textView.setText(money);
            textView.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        } else {
            view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        }
    }

    void to(){
//        String money = String.valueOf(mGoldTotal);
//        money.length()> 3? money.replace();
//        money.indexOf(money.length()-3);
    }

    private void playSound() {
        Debug.anchor("mVolume:" + mVolume);
        mSoundPool.play(
                mSoundID,
                mVolume,      //左耳道音量【0~1】
                mVolume,      //右耳道音量【0~1】
                3,         //播放优先级【0表示最低优先级】
                1,         //循环模式【0表示循环一次，-1表示一直循环，其他表示数字+1表示当前数字对应的循环次数】
                1          //播放速度【1是正常，范围从0~2】
        );
    }

    public void onSendVipGift(View view) {
        Claim.show(this);
    }

    public void onGoAndOn(View view) {
        goOnLayoutAddView();
    }

    void goOnLayoutAddView() {
        final LinearLayout goAndOnLayout = (LinearLayout) findViewById(R.id.goAndOnLayout);
        final View inflate = getLayoutInflater().inflate(R.layout.test_anim_gift_item, null);

        if (goAndOnLayout.getChildCount() == 2) {
            final View view = goAndOnLayout.getChildAt(1);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_to_right);
            animation.setAnimationListener(new SimpleAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    goAndOnLayout.removeView(view);
                    goAndOnLayout.addView(inflate, 0);
                    inflate.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_in_from_left));
                }
            });
            view.startAnimation(animation);
        } else {
            goAndOnLayout.addView(inflate, 0);
            inflate.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left));
        }
    }

    public void onGoAndDown(View view) {
        goOnLayoutRemoveView();
    }

    void goOnLayoutRemoveView() {
        final LinearLayout goAndOnLayout = (LinearLayout) findViewById(R.id.goAndOnLayout);

        if (goAndOnLayout.getChildCount() > 0) {
            final View view = goAndOnLayout.getChildAt(goAndOnLayout.getChildCount() - 1);
            goAndOnLayout.removeView(view);
        }
    }

    public void onGoAndAuto(View v) {
        final LinearLayout autoGoonLayout = (LinearLayout) findViewById(R.id.autoGoonLayout);
        final View inflate = getLayoutInflater().inflate(R.layout.test_anim_gift_item, null);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_left_out_to_right);
        animation.setAnimationListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                autoGoonLayout.removeView(inflate);
            }
        });
        autoGoonLayout.addView(inflate, 0);
        inflate.startAnimation(animation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSoundPool.release();
    }


    class SimpleAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
