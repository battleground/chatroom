package com.abooc.im.notifications;

import java.util.List;

/**
 * Created by dayu on 2017/2/16.
 */

public class MVP {

    public interface Viewer {

        void showList(List<?> list);
    }

    public interface Notifications {

        void load();
    }

}
