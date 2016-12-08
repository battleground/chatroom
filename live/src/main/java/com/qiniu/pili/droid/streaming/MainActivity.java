package com.qiniu.pili.droid.streaming;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.leancloud.im.chatroom.AVIMClientManager;
import com.leancloud.im.chatroom.activity.ChatRoomsActivity;
import com.qiniu.pili.droid.streaming.commons.utils.ToastUtils;
import com.qiniu.pili.droid.streaming.demo.Config;
import com.qiniu.pili.droid.streaming.demo.R;
import com.qiniu.pili.droid.streaming.live.bean.ReqLiveBean;
import com.qiniu.pili.droid.streaming.live.bean.RespCreateLiveBean;
import com.qiniu.pili.droid.streaming.live.bean.RespLookLiveBean;
import com.qiniu.pili.droid.streaming.live.presenter.LivePresenter;
import com.qiniu.pili.droid.streaming.live.view.CreateLiveView;
import com.qiniu.pili.droid.streaming.play.LiveRoomActivity;
import com.qiniu.pili.droid.streaming.play.PlayActivity;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.qiniu.pili.droid.streaming.commons.utils.ToastUtils.showToast;


/**
 * 开直播、观看入口
 */
public class MainActivity extends AppCompatActivity implements CreateLiveView {

    private LivePresenter mLivePresenter = new LivePresenter(this);
    private boolean isPermissionOK;


    public static void launch(Context ctx) {
        Intent intent = new Intent(ctx, MainActivity.class);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getTitle() + " - " + AVIMClientManager.getInstance().getClientId());

        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        setPermission();

    }


    /**
     * 设置权限
     */
    private void setPermission() {
        new TedPermission(this).setPermissionListener(new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                ToastUtils.showToast("授权成功");
                isPermissionOK = true;
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                isPermissionOK = false;
            }
        }).setDeniedMessage("")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    @OnClick({R.id.button, R.id.button2})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                mLivePresenter.startLive();
                break;
            case R.id.button2:
                mLivePresenter.lookLive();
                break;
        }
    }

    @Override
    public ReqLiveBean getReqLiveBean(int tag) {
        ReqLiveBean reqLiveBean = new ReqLiveBean();
        if (tag == LivePresenter.STARTLIVE)
            reqLiveBean.setMethod("fm.live.create");
        else
            reqLiveBean.setMethod("fm.live.get");
        reqLiveBean.setSys_version("V1.0.23");
        reqLiveBean.setStreamKey("10001live");
        return reqLiveBean;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toLCLKRecordActivity(final RespCreateLiveBean respCreateLiveBean) {

        createLive(respCreateLiveBean);

    }

    @Override
    public void toLCLKPlayActivity(RespLookLiveBean respLookLiveBean) {

        lookLive(respLookLiveBean);

    }

    // 进入直播
    private void createLive(final RespCreateLiveBean respCreateLiveBean) {
        Intent intent = new Intent(MainActivity.this, LiveRoomActivity.class);
        startStreamingActivity(respCreateLiveBean.getData().getPublishurl(), intent);

    }

    //观看直播
    private void lookLive(RespLookLiveBean respLookLiveBean) {
//        Intent intent = new Intent(MainActivity.this, PlayActivity.class);
//        intent.putExtra(PlayActivity.PALY_LIVE,respLookLiveBean.getData().getRTMP());
//        startActivity(intent);
        PlayActivity.launch(this, respLookLiveBean.getData().getRTMP(), "58411255128fe1005898c163");
    }

    @Override
    public void showFailedError() {
        showToast("获取失败！");
    }


    private void startStreamingActivity(final String inputUrl, final Intent intent) {
        if (!isPermissionOK) {
            return;
        }
        String publishUrl = null;
        if (!"".equalsIgnoreCase(inputUrl)) {
            publishUrl = Config.EXTRA_PUBLISH_URL_PREFIX + inputUrl;
        }
        if (publishUrl == null) {
            showToast("Publish Url Got Fail!");
            return;
        }
        intent.putExtra(Config.EXTRA_KEY_PUB_URL, publishUrl);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
