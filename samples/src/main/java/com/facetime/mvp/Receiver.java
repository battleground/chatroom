package com.facetime.mvp;

import com.abooc.im.message.CallMessage;
import com.avos.avoscloud.im.v2.AVIMMessageManager;

/**
 * Created by dayu on 2017/10/18.
 */

public class Receiver {

    FaceTimePresenter.CustomMessageHandler iCustomMessageHandler;

    public void onCreate(FaceTimeViewer viewer) {
        iCustomMessageHandler = new FaceTimePresenter.CustomMessageHandler(viewer);
        AVIMMessageManager.registerMessageHandler(CallMessage.class, iCustomMessageHandler);
    }

    public void destroy() {
        AVIMMessageManager.unregisterMessageHandler(CallMessage.class, iCustomMessageHandler);
    }
}
