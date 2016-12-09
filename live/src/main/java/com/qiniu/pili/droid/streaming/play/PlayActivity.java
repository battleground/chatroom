package com.qiniu.pili.droid.streaming.play;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.leancloud.im.chatroom.Constants;
import com.qiniu.pili.droid.streaming.demo.R;

/**
 * Created by wli on 16/8/5.
 */
public class PlayActivity extends AppCompatActivity {

    public static final String PALY_LIVE = "play_live";

    public static void launch(Context context, String playLive, String conversationId) {
        Intent intent = new Intent(context, PlayActivity.class);
        intent.putExtra(PlayActivity.PALY_LIVE, playLive);
        intent.putExtra(Constants.CONVERSATION_ID, conversationId);
        context.startActivity(intent);
    }

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
