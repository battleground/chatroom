package com.qiniu.pili.droid.streaming.live.view;

import com.qiniu.pili.droid.streaming.live.bean.ReqLiveBean;
import com.qiniu.pili.droid.streaming.live.bean.RespCreateLiveBean;
import com.qiniu.pili.droid.streaming.live.bean.RespLookLiveBean;

/**
 * Created by Administrator on 2016/12/6.
 */

public interface CreateLiveView {

    ReqLiveBean getReqLiveBean(int tag);

    void showLoading();

    void hideLoading();

    void toLCLKRecordActivity(RespCreateLiveBean respCreateLiveBean);

    void toLCLKPlayActivity(RespLookLiveBean respLookLiveBean);

    void showFailedError();

}
