package com.qiniu.pili.droid.streaming.live.bean;


import com.qiniu.pili.droid.streaming.commons.bean.BaseBean;

/**
 * Created by Administrator on 2016/12/6.
 */

public class ReqLiveBean extends BaseBean {

    private String streamKey;

    public String getStreamKey() {
        return streamKey;
    }

    public void setStreamKey(String streamKey) {
        this.streamKey = streamKey;
    }
}
