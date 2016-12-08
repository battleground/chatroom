package com.qiniu.pili.droid.streaming.live.presenter;


import com.qiniu.pili.droid.streaming.live.bean.RespCreateLiveBean;
import com.qiniu.pili.droid.streaming.live.bean.RespLookLiveBean;
import com.qiniu.pili.droid.streaming.live.model.LiveModel;
import com.qiniu.pili.droid.streaming.live.model.OnLiveCreateListener;
import com.qiniu.pili.droid.streaming.live.model.OnLiveLookListener;
import com.qiniu.pili.droid.streaming.live.view.CreateLiveView;

/**
 * Created by Administrator on 2016/12/6.
 */

public class LivePresenter {

    public static final int STARTLIVE = 1;
    public static final int LOOKLIVE = 2;

    private LiveModel liveModel;
    private CreateLiveView createLiveView;

    public LivePresenter(CreateLiveView createLiveView) {
        this.createLiveView = createLiveView;
        this.liveModel = new LiveModel();
    }

    /**
     * 开始直播
     */
    public void startLive() {
        createLiveView.showLoading();
        liveModel.createLive(createLiveView.getReqLiveBean(STARTLIVE), new OnLiveCreateListener() {
            @Override
            public void createLiveSuccess(RespCreateLiveBean respCreateLiveBean) {
                createLiveView.toLCLKRecordActivity(respCreateLiveBean);
            }

            @Override
            public void createLiveFailed() {
                createLiveView.showFailedError();
            }

            @Override
            public void onComplete() {
                createLiveView.hideLoading();
            }
        });
    }

    /**
     * 观看直播
     */
    public void lookLive() {
        createLiveView.showLoading();
        liveModel.lookLive(createLiveView.getReqLiveBean(LOOKLIVE), new OnLiveLookListener() {
            @Override
            public void lookLiveSuccess(RespLookLiveBean respLookLiveBean) {
                createLiveView.toLCLKPlayActivity(respLookLiveBean);
            }

            @Override
            public void lookLiveFailed() {
                createLiveView.showFailedError();
            }

            @Override
            public void onComplete() {
                createLiveView.hideLoading();
            }
        });


    }

}
