package com.qiniu.pili.droid.streaming.live.model;


import com.qiniu.pili.droid.streaming.live.bean.ReqLiveBean;

/**
 * Created by Administrator on 2016/12/6.
 */

public interface ILiveModel {

    /**
     * 创建直播
     *
     * @param reqLiveBean 请求参数对象
     * @param listener    回调监听
     */
    void createLive(ReqLiveBean reqLiveBean, OnLiveCreateListener listener);

    /**
     * 播放
     *
     * @param reqLiveBean
     * @param listener
     */
    void lookLive(ReqLiveBean reqLiveBean, OnLiveLookListener listener);

}
