package com.abooc.liveroom;

import android.content.Context;
import android.hardware.Camera;

import com.abooc.util.Debug;
import com.qiniu.android.dns.DnsManager;
import com.qiniu.android.dns.IResolver;
import com.qiniu.android.dns.NetworkInfo;
import com.qiniu.android.dns.http.DnspodFree;
import com.qiniu.android.dns.local.AndroidDnsServer;
import com.qiniu.android.dns.local.Resolver;
import com.qiniu.pili.droid.streaming.AVCodecType;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qiniu.pili.droid.streaming.MediaStreamingManager;
import com.qiniu.pili.droid.streaming.MicrophoneStreamingSetting;
import com.qiniu.pili.droid.streaming.StreamingProfile;
import com.qiniu.pili.droid.streaming.WatermarkSetting;
import com.qiniu.pili.droid.streaming.widget.AspectFrameLayout;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;

/**
 * Created by dayu on 2016/12/26.
 */
public class MediaStream {

    public static final int ENCODING_LEVEL = StreamingProfile.VIDEO_ENCODING_HEIGHT_480;

    String publishUrl = "rtmp://pili-publish.fengmi.tv/bftv/obama?e=1482750016&token=5QUyMYeIxq2nFE-cpZ6pELwHVO58hO8uRf44UEQR:4e47KgL2Fh3ewVuoGNvW20JaWiU=";


    MediaStreamingManager mMediaStreamingManager;
    CameraStreamingSetting mCameraStreamingSetting;
    MicrophoneStreamingSetting mMicrophoneStreamingSetting;
    StreamingProfile mProfile;
    WatermarkSetting mWatermarkSetting;


    private static Context mContext;

    private static MediaStream ourInstance = new MediaStream();

    public static MediaStream buildUI(Context context, AspectFrameLayout afl, CameraPreviewFrameView cameraPreviewFrameView) {
        mContext = context;
        ourInstance.mMediaStreamingManager = new MediaStreamingManager(mContext, afl, cameraPreviewFrameView, AVCodecType.HW_VIDEO_CODEC); // hw codec
        ourInstance.mMediaStreamingManager.setNativeLoggingEnabled(false);

        return ourInstance;
    }

    public static MediaStream get() {
        return ourInstance;
    }

    private MediaStream() {

//        mMediaStreamingManager.updateEncodingType(AVCodecType.HW_VIDEO_CODEC);

    }

    public MediaStream buildStreamingSetting() {
        CameraStreamingSetting.CAMERA_FACING_ID cameraFacingId = chooseCameraFacingId();
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


        mProfile = new StreamingProfile();
        try {
            mProfile.setPublishUrl(publishUrl);
        } catch (URISyntaxException e) {
            Debug.error();
            e.printStackTrace();
        }
        return this;
    }

    public MediaStream buildProfile() {
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
        return this;
    }

    public MediaStream buildWatermark() {
        mWatermarkSetting = new WatermarkSetting(mContext);
        mWatermarkSetting.setResourceId(R.drawable.qiniu_logo)
                .setSize(WatermarkSetting.WATERMARK_SIZE.MEDIUM)
                .setAlpha(90)
                .setCustomPosition(0.5f, 0.5f);
        return this;
    }

    public MediaStreamingManager getMediaStreamingManager() {
//        mMediaStreamingManager.setStreamingStateListener(this);
////        mMediaStreamingManager.setSurfaceTextureCallback(this);
//        mMediaStreamingManager.setStreamingSessionListener(this);
////        mMediaStreamingManager.setNativeLoggingEnabled(false);
////        mMediaStreamingManager.setStreamStatusCallback(this);
////        mMediaStreamingManager.setStreamingPreviewCallback(this);
        return mMediaStreamingManager;
    }

    public MediaStreamingManager prepare() {
        mMediaStreamingManager.prepare(mCameraStreamingSetting, mMicrophoneStreamingSetting, mWatermarkSetting, mProfile);
        return mMediaStreamingManager;
    }

    private CameraStreamingSetting.CAMERA_FACING_ID chooseCameraFacingId() {
        if (CameraStreamingSetting.hasCameraFacing(CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_3RD)) {
            return CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_3RD;
        } else if (CameraStreamingSetting.hasCameraFacing(CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT)) {
            return CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT;
        } else {
            return CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK;
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

}
