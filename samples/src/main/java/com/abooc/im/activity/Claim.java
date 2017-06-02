package com.abooc.im.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;

import com.abooc.im.R;


/**
 * 显示领取金币效果
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-5-22.
 */
public class Claim extends Activity {

    /**
     * 显示领取金币效果
     *
     * @param activity
     */
    public static void show(Activity activity) {
        Intent intent = new Intent(activity, Claim.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    private SoundPool mSoundPool;
    private int mSoundID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim);

        View claimBackground = findViewById(R.id.ClaimBackground);
        View BrightView = findViewById(R.id.Bright);
        View BrightView2 = findViewById(R.id.Bright2);


        Animation mBreatheAnimationEnter = AnimationUtils.loadAnimation(this, R.anim.founder_label_scale_enter);
        mBreatheAnimationEnter.setRepeatCount(Animation.INFINITE);
        mBreatheAnimationEnter.setRepeatMode(Animation.INFINITE);
        mBreatheAnimationEnter.setDuration(3 * 1000);
        BrightView.setAnimation(mBreatheAnimationEnter);
        BrightView2.setAnimation(mBreatheAnimationEnter);

        claimBackground.setAnimation(createHintSwitchAnimation(false));

        mSoundPool = new SoundPool(5, AudioManager.STREAM_RING, 0);
        mSoundID = mSoundPool.load(getApplicationContext(), R.raw.diaoluo_da, 1);

        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Claim.super.finish();
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        }.sendEmptyMessageDelayed(0, 3 * 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        playSound();

    }

    private Animation createHintSwitchAnimation(final boolean expanded) {
        Animation animation = new RotateAnimation(
                expanded ? 360 : 0, expanded ? 0 : 360
                , Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        );
        animation.setRepeatMode(Animation.INFINITE);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setStartOffset(0);
        animation.setDuration(30 * 1000);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setFillAfter(true);
        return animation;
    }

    private void playSound() {
        mSoundPool.play(
                mSoundID,
                1.0f,      //左耳道音量【0~1】
                1.0f,      //右耳道音量【0~1】
                5,         //播放优先级【0表示最低优先级】
                1,         //循环模式【0表示循环一次，-1表示一直循环，其他表示数字+1表示当前数字对应的循环次数】
                1          //播放速度【1是正常，范围从0~2】
        );
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSoundPool.release();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

}
