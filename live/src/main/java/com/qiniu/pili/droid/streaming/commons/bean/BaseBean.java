package com.qiniu.pili.droid.streaming.commons.bean;

/**
 * Created by Administrator on 2016/12/5.
 */

public class BaseBean {
    private String sys_version;
    private String method;

    private final String apptoken = "282340ce12c5e10fa84171660a2054f8";
    private final String version = "2.0";
    private final String from = "android";

    public String getApptoken() {
        return apptoken;
    }

    public String getVersion() {
        return version;
    }

    public String getFrom() {
        return from;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSys_version() {
        return sys_version;
    }

    public void setSys_version(String sys_version) {
        this.sys_version = sys_version;
    }

}
