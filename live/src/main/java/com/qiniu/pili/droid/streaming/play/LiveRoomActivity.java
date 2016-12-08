package com.qiniu.pili.droid.streaming.play;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.qiniu.pili.droid.streaming.demo.HWCodecCameraStreamingActivity;
import com.qiniu.pili.droid.streaming.demo.R;

/**
 * Created by dayu on 2016/12/8.
 */

public class LiveRoomActivity extends HWCodecCameraStreamingActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_live_room, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_capture:
                break;
            case R.id.action_front_camera:
                break;
            case R.id.action_quit_room:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
