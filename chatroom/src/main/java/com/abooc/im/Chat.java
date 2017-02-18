package com.abooc.im;

import com.abooc.util.Debug;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

/**
 * Created by dayu on 2017/1/10.
 */

public class Chat {

    AVIMClient tom = AVIMClient.getInstance("Jerry");

    void login() {
        AVIMClient tom = AVIMClient.getInstance("Tom");
        tom.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if (e == null) {
                    Debug.anchor("登录：成功");

//                    join();
                } else
                    Debug.anchor("登录：" + e);
            }
        });
    }
}
