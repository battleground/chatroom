package com.qiniu.pili.droid.streaming.live.model;

import com.qiniu.pili.droid.streaming.live.bean.RespCreateLiveBean;

/**
 * Created by Administrator on 2016/12/6.
 */

public interface OnLiveCreateListener {
    void createLiveSuccess(RespCreateLiveBean respCreateLiveBean);
    void createLiveFailed();
    void onComplete();
}
