package com.qiniu.pili.droid.streaming.live.bean;


import com.qiniu.pili.droid.streaming.commons.bean.RespBaseBean;

/**
 * Created by Administrator on 2016/12/6.
 */

public class RespCreateLiveBean extends RespBaseBean {

    /**
     * status : 200
     * data : {"hub":"bftv","key":"10001live","disabledTill":"0","publishurl":"rtmp://pili-publish.fengmi.tv/bftv/10001live?e=1481023089&token=5QUyMYeIxq2nFE-cpZ6pELwHVO58hO8uRf44UEQR:AnUkswt313cSiVpI6_HXnkxIzGo="}
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
         * hub : bftv
         * key : 10001live
         * disabledTill : 0
         * publishurl : rtmp://pili-publish.fengmi.tv/bftv/10001live?e=1481023089&token=5QUyMYeIxq2nFE-cpZ6pELwHVO58hO8uRf44UEQR:AnUkswt313cSiVpI6_HXnkxIzGo=
         */

        private String hub;
        private String key;
        private String disabledTill;
        private String publishurl;

        public String getHub() {
            return hub;
        }

        public void setHub(String hub) {
            this.hub = hub;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getDisabledTill() {
            return disabledTill;
        }

        public void setDisabledTill(String disabledTill) {
            this.disabledTill = disabledTill;
        }

        public String getPublishurl() {
            return publishurl;
        }

        public void setPublishurl(String publishurl) {
            this.publishurl = publishurl;
        }
    }
}
