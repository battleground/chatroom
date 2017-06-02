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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSoundPool.release();
    }

}
