package com.qiniu.pili.droid.streaming.play;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.qiniu.pili.droid.streaming.demo.R;

/**
 * Created by wli on 16/8/5.
 */
public class PlayActivity extends FragmentActivity {

    public static  final  String PALY_LIVE = "play_live";

    PlayFragment playFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lclk_play_activity);
        playFragment = (PlayFragment) getSupportFragmentManager().findFragmentById(R.id.lclk_play_fragment);
        String s = getIntent().getStringExtra(PALY_LIVE);
        playFragment.startLive(s);
    }
}
