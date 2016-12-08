package com.qiniu.pili.droid.streaming.commons.data;

import android.util.Log;

import com.google.gson.Gson;
import com.qiniu.pili.droid.streaming.commons.bean.RespBaseBean;
import com.qiniu.pili.droid.streaming.commons.config.LCLKApplication;
import com.qiniu.pili.droid.streaming.commons.utils.ToastUtils;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;


/**
 * 通用网络回调类
 */
public abstract class MyStringCallback extends Callback<RespBaseBean> {

    private Class<? extends RespBaseBean> bc;
    private String jsonString;

    public MyStringCallback(Class<? extends RespBaseBean> bc) {
        this.bc = bc;
    }

    //将Json数据解析成相应的映射对象
    private <T> T parseJsonWithGson(String jsonData, Class<T> type) {
        Gson gson = new Gson();
        T result = gson.fromJson(jsonData, type);
        return result;
    }

    @Override
    public RespBaseBean parseNetworkResponse(Response response, int id) throws Exception {
        jsonString = response.body().string();
        Log.e("JS", "返回json ------>" + jsonString);
        return parseJsonWithGson(jsonString, bc);
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        onFail(id);
        onComplete();
        if (!LCLKApplication.hasNetwork())
            ToastUtils.showToast("网络异常,请检测网络连接！");
        if ((e + "").contains("SocketTimeoutException"))
            ToastUtils.showToast("网络连接超时！");
    }

    @Override
    public void onResponse(RespBaseBean response, int id) {
        if (response != null)
            onSuccess(response, id);
        else
            onFail(id);
        onComplete();
    }

    public abstract void onSuccess(RespBaseBean bean, int id);

    public abstract void onFail(int id);


    public void onComplete() {
    }

}
