package com.abooc.im.notifications;

import com.abooc.im.notifications.MVP.Notifications;

/**
 * Created by dayu on 2017/2/16.
 */

public class NotificationsPresenter implements Notifications {


    MVP.Viewer iViewer;

    public NotificationsPresenter(MVP.Viewer viewer) {
        iViewer = viewer;
    }

    @Override
    public void load() {

        iViewer.showList(null);
    }


}
