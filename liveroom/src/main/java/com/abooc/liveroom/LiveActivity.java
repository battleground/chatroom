package com.abooc.liveroom;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.abooc.util.Debug;
import com.qiniu.android.dns.DnsManager;
import com.qiniu.android.dns.IResolver;
import com.qiniu.android.dns.NetworkInfo;
import com.qiniu.android.dns.http.DnspodFree;
import com.qiniu.android.dns.local.AndroidDnsServer;
import com.qiniu.android.dns.local.Resolver;
import com.qiniu.pili.droid.streaming.AVCodecType;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting.CAMERA_FACING_ID;
import com.qiniu.pili.droid.streaming.MediaStreamingManager;
import com.qiniu.pili.droid.streaming.MicrophoneStreamingSetting;
import com.qiniu.pili.droid.streaming.StreamingProfile;
import com.qiniu.pili.droid.streaming.StreamingSessionListener;
import com.qiniu.pili.droid.streaming.StreamingState;
import com.qiniu.pili.droid.streaming.StreamingStateChangedListener;
import com.qiniu.pili.droid.streaming.WatermarkSetting;
import com.qiniu.pili.droid.streaming.widget.AspectFrameLayout;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.util.List;


public class LiveActivity extends AppCompatActivity implements
        CameraPreviewFrameView.Listener,
        StreamingSessionListener,
        StreamingStateChangedListener {


    public static void launch(Context ctx) {
        Intent intent = new Intent(ctx, LiveActivity.class);
        ctx.startActivity(intent);
    }

    public static final int ENCODING_LEVEL = StreamingProfile.VIDEO_ENCODING_HEIGHT_480;

    MediaStreamingManager mMediaStreamingManager;
    CameraStreamingSetting mCameraStreamingSetting;
    MicrophoneStreamingSetting mMicrophoneStreamingSetting;
    StreamingProfile mProfile;

    private CAMERA_FACING_ID chooseCameraFacingId() {
        if (CameraStreamingSetting.hasCameraFacing(CAMERA_FACING_ID.CAMERA_FACING_3RD)) {
            return CAMERA_FACING_ID.CAMERA_FACING_3RD;
        } else if (CameraStreamingSetting.hasCameraFacing(CAMERA_FACING_ID.CAMERA_FACING_FRONT)) {
            return CAMERA_FACING_ID.CAMERA_FACING_FRONT;
        } else {
            return CAMERA_FACING_ID.CAMERA_FACING_BACK;
        }
    }

    private static DnsManager getMyDnsManager() {
        IResolver r0 = new DnspodFree();
        IResolver r1 = AndroidDnsServer.defaultResolver();
        IResolver r2 = null;
        try {
            r2 = new Resolver(InetAddress.getByName("119.29.29.29"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new DnsManager(NetworkInfo.normal, new IResolver[]{r0, r1, r2});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CAMERA_FACING_ID cameraFacingId = chooseCameraFacingId();
        mCameraStreamingSetting = new CameraStreamingSetting();
        mCameraStreamingSetting.setCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT)
                .setContinuousFocusModeEnabled(true)
                .setRecordingHint(false)
                .setCameraFacingId(cameraFacingId)
                .setBuiltInFaceBeautyEnabled(true)
//                .setCameraSourceImproved(true)
//                .setCaptureCameraFrameOnly(true)
                .setResetTouchFocusDelayInMs(3000)
//                .setFocusMode(CameraStreamingSetting.FOCUS_MODE_CONTINUOUS_PICTURE)
                .setCameraPrvSizeLevel(CameraStreamingSetting.PREVIEW_SIZE_LEVEL.SMALL)
                .setCameraPrvSizeRatio(CameraStreamingSetting.PREVIEW_SIZE_RATIO.RATIO_16_9)
                .setFaceBeautySetting(new CameraStreamingSetting.FaceBeautySetting(1.0f, 1.0f, 0.8f))
                .setVideoFilter(CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY);

        mMicrophoneStreamingSetting = new MicrophoneStreamingSetting();
        mMicrophoneStreamingSetting.setBluetoothSCOEnabled(false);

        String publishUrl = "rtmp://pili-publish.fengmi.tv/bftv/obama?e=1482312691&token=5QUyMYeIxq2nFE-cpZ6pELwHVO58hO8uRf44UEQR:gFPyB8QrpTmb3ZFAPPZzaI8RMFc=";

        mProfile = new StreamingProfile();
        try {
            mProfile.setPublishUrl(publishUrl);
        } catch (URISyntaxException e) {
            Debug.error();
            e.printStackTrace();
        }

        mProfile.setVideoQuality(StreamingProfile.VIDEO_QUALITY_MEDIUM2)
                .setAudioQuality(StreamingProfile.AUDIO_QUALITY_MEDIUM2)
//                .setPreferredVideoEncodingSize(960, 544)
                .setEncodingSizeLevel(ENCODING_LEVEL)
                .setEncoderRCMode(StreamingProfile.EncoderRCModes.BITRATE_PRIORITY)
//                .setAVProfile(avProfile)
                .setDnsManager(getMyDnsManager())
                .setAdaptiveBitrateEnable(true)
                .setFpsControllerEnable(true)
                .setStreamStatusConfig(new StreamingProfile.StreamStatusConfig(3))
                .setEncodingOrientation(StreamingProfile.ENCODING_ORIENTATION.PORT)
                .setSendingBufferProfile(new StreamingProfile.SendingBufferProfile(0.2f, 0.8f, 3.0f, 20 * 1000));

        AspectFrameLayout afl = (AspectFrameLayout) findViewById(R.id.cameraPreview_afl);
        afl.setShowMode(AspectFrameLayout.SHOW_MODE.REAL);
        CameraPreviewFrameView cameraPreviewFrameView =
                (CameraPreviewFrameView) findViewById(R.id.cameraPreview_surfaceView);
        cameraPreviewFrameView.setListener(this);

        WatermarkSetting watermarksetting = new WatermarkSetting(this);
        watermarksetting.setResourceId(R.drawable.qiniu_logo)
                .setSize(WatermarkSetting.WATERMARK_SIZE.MEDIUM)
                .setAlpha(90)
                .setCustomPosition(0.5f, 0.5f);

        mMediaStreamingManager = new MediaStreamingManager(this, afl, cameraPreviewFrameView, AVCodecType.HW_VIDEO_CODEC); // hw codec
//        mMediaStreamingManager.prepare(mCameraStreamingSetting, mMicrophoneStreamingSetting, watermarksetting, mProfile, new PreviewAppearance(0.0f, 0.0f, 0.5f, 0.5f, PreviewAppearance.ScaleType.FIT));
        mMediaStreamingManager.prepare(mCameraStreamingSetting, mMicrophoneStreamingSetting, watermarksetting, mProfile);

        mMediaStreamingManager.setStreamingStateListener(this);
//        mMediaStreamingManager.setSurfaceTextureCallback(this);
        mMediaStreamingManager.setStreamingSessionListener(this);
//        mMediaStreamingManager.setNativeLoggingEnabled(false);
//        mMediaStreamingManager.setStreamStatusCallback(this);
//        mMediaStreamingManager.setStreamingPreviewCallback(this);


//        mMediaStreamingManager.updateEncodingType(AVCodecType.HW_VIDEO_CODEC);


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
    public void onStateChanged(StreamingState streamingState, Object extra) {
        Debug.error("StreamingState streamingState:" + streamingState + ",extra:" + extra);

        switch (streamingState) {
            case PREPARING:
                break;
            case READY:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean res = mMediaStreamingManager.startStreaming();
                        Debug.anchor("res:" + res);
                    }
                }).start();
                break;
        }

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
