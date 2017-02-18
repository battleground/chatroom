package com.abooc.liveroom;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.abooc.util.Debug;
import com.qiniu.pili.droid.streaming.MediaStreamingManager;
import com.qiniu.pili.droid.streaming.StreamingSessionListener;
import com.qiniu.pili.droid.streaming.StreamingState;
import com.qiniu.pili.droid.streaming.StreamingStateChangedListener;
import com.qiniu.pili.droid.streaming.widget.AspectFrameLayout;

import java.util.List;


public class LiveActivity extends AppCompatActivity implements
        CameraPreviewFrameView.Listener,
        StreamingSessionListener,
        StreamingStateChangedListener {


    public static void launch(Context ctx) {
        Intent intent = new Intent(ctx, LiveActivity.class);
        ctx.startActivity(intent);
    }

    MediaStreamingManager mMediaStreamingManager;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.start);

        AspectFrameLayout afl = (AspectFrameLayout) findViewById(R.id.cameraPreview_afl);
        afl.setShowMode(AspectFrameLayout.SHOW_MODE.REAL);
        CameraPreviewFrameView cameraPreviewFrameView = (CameraPreviewFrameView) findViewById(R.id.cameraPreview_surfaceView);
        cameraPreviewFrameView.setListener(this);

        mMediaStreamingManager = MediaStream
                .buildUI(this, afl, cameraPreviewFrameView)
                .buildStreamingSetting()
                .buildWatermark()
                .buildProfile()
                .prepare();
        mMediaStreamingManager.setStreamingStateListener(this);
//        mMediaStreamingManager.setSurfaceTextureCallback(this);
        mMediaStreamingManager.setStreamingSessionListener(this);
//        mMediaStreamingManager.setStreamStatusCallback(this);
//        mMediaStreamingManager.setStreamingPreviewCallback(this);
    }

    public void onStartEvent(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean res = mMediaStreamingManager.startStreaming();
                Debug.anchor("res:" + res);
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaStreamingManager.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaStreamingManager.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaStreamingManager.destroy();
    }

    @Override
    public void onStateChanged(final StreamingState streamingState, Object extra) {
        Debug.error("StreamingState streamingState:" + streamingState + ",extra:" + extra);

        switch (streamingState) {
            case PREPARING:
                break;
            case READY:
                break;
            default:
                break;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(streamingState.toString());
            }
        });
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onZoomValueChanged(float factor) {
        return false;
    }

    @Override
    public boolean onRecordAudioFailedHandled(int i) {
        return false;
    }

    @Override
    public boolean onRestartStreamingHandled(int i) {
        return false;
    }

    @Override
    public Camera.Size onPreviewSizeSelected(List<Camera.Size> list) {
        return null;
    }

}
