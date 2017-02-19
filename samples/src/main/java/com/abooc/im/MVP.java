package com.abooc.im;

import com.avos.avoscloud.im.v2.AVIMMessage;

import java.util.List;

/**
 * Created by dayu on 2017/1/10.
 */

public class MVP {

    public interface HomeViewer {
        void showList(List<AVIMMessage> list);

        void showMessage(AVIMMessage message);
    }
}
