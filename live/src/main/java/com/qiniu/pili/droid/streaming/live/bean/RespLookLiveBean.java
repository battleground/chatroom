package com.qiniu.pili.droid.streaming.live.bean;

import com.qiniu.pili.droid.streaming.commons.bean.RespBaseBean;

/**
 * Created by Administrator on 2016/12/6.
 */

public class RespLookLiveBean extends RespBaseBean {


    /**
     * status : 200
     * data : {"RTMP":"rtmp://pili-live-rtmp.fengmi.tv/bftv/10001live","HLS":"http://pili-live-hls.fengmi.tv/bftv/10001live.m3u8","HDL":"http://pili-live-hdl.fengmi.tv/bftv/10001live.flv","SnapShot":"http://pili-live-snapshot.fengmi.tv/bftv/10001live.jpg"}
     */

    private int status;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * RTMP : rtmp://pili-live-rtmp.fengmi.tv/bftv/10001live
         * HLS : http://pili-live-hls.fengmi.tv/bftv/10001live.m3u8
         * HDL : http://pili-live-hdl.fengmi.tv/bftv/10001live.flv
         * SnapShot : http://pili-live-snapshot.fengmi.tv/bftv/10001live.jpg
         */

        private String RTMP;
        private String HLS;
        private String HDL;
        private String SnapShot;

        public String getRTMP() {
            return RTMP;
        }

        public void setRTMP(String RTMP) {
            this.RTMP = RTMP;
        }

        public String getHLS() {
            return HLS;
        }

        public void setHLS(String HLS) {
            this.HLS = HLS;
        }

        public String getHDL() {
            return HDL;
        }

        public void setHDL(String HDL) {
            this.HDL = HDL;
        }

        public String getSnapShot() {
            return SnapShot;
        }

        public void setSnapShot(String SnapShot) {
            this.SnapShot = SnapShot;
        }
    }
}
