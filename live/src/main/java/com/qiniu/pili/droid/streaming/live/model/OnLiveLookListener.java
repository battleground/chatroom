package com.qiniu.pili.droid.streaming.live.model;


import com.qiniu.pili.droid.streaming.live.bean.RespLookLiveBean;

/**
 * Created by Administrator on 2016/12/6.
 */

public interface OnLiveLookListener {
    void lookLiveSuccess(RespLookLiveBean respLookLiveBean);
    void lookLiveFailed();
    void onComplete();
}
