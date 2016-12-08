package com.qiniu.pili.droid.streaming.live.model;

import com.qiniu.pili.droid.streaming.commons.bean.RespBaseBean;
import com.qiniu.pili.droid.streaming.commons.config.Constacts;
import com.qiniu.pili.droid.streaming.commons.data.MyStringCallback;
import com.qiniu.pili.droid.streaming.live.bean.ReqLiveBean;
import com.qiniu.pili.droid.streaming.live.bean.RespCreateLiveBean;
import com.qiniu.pili.droid.streaming.live.bean.RespLookLiveBean;
import com.zhy.http.okhttp.OkHttpUtils;


/**
 * Created by Administrator on 2016/12/6.
 * application/x-www-form-urlencoded
 */

public class LiveModel implements ILiveModel {

    @Override
    public void createLive(ReqLiveBean reqLiveBean, final OnLiveCreateListener listener) {
       /* OkHttpUtils.postString()
                .url(Constacts.DEVAPI).content(new Gson().toJson(reqLiveBean))
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new MyStringCallback(RespCreateLiveBean.class) {
                    @Override
                    public void onSuccess(BaseBean bean, int id) {
                        listener.createLiveSuccess((RespCreateLiveBean)bean);
                    }

                    @Override
                    public void onFail(String code, int id) {
                        listener.createLiveFailed();
                    }
                });*/
        OkHttpUtils.post().url(Constacts.DEVAPI)
                .addParams("streamKey", reqLiveBean.getStreamKey())
                .addParams("apptoken", reqLiveBean.getApptoken())
                .addParams("from", reqLiveBean.getFrom())
                .addParams("method", reqLiveBean.getMethod())
                .addParams("sys_version", reqLiveBean.getSys_version())
                .addParams("version", reqLiveBean.getVersion())
                .build()
                .execute(new MyStringCallback(RespCreateLiveBean.class) {
                    @Override
                    public void onSuccess(RespBaseBean bean, int id) {
                        listener.createLiveSuccess((RespCreateLiveBean) bean);
                    }

                    @Override
                    public void onFail(int id) {
                        listener.createLiveFailed();
                    }
                });

    }

    @Override
    public void lookLive(ReqLiveBean reqLiveBean, final OnLiveLookListener listener) {

        OkHttpUtils.post().url(Constacts.DEVAPI)
                .addParams("streamKey", reqLiveBean.getStreamKey())
                .addParams("apptoken", reqLiveBean.getApptoken())
                .addParams("from", reqLiveBean.getFrom())
                .addParams("method", reqLiveBean.getMethod())
                .addParams("sys_version", reqLiveBean.getSys_version())
                .addParams("version", reqLiveBean.getVersion())
                .build()
                .execute(new MyStringCallback(RespLookLiveBean.class) {
                    @Override
                    public void onSuccess(RespBaseBean bean, int id) {
                        listener.lookLiveSuccess((RespLookLiveBean) bean);
                    }

                    @Override
                    public void onFail(int id) {
                        listener.lookLiveFailed();
                    }
                });
    }


}
