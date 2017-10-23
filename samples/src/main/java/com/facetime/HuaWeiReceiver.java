package com.facetime;

import android.content.Context;
import android.content.Intent;

import com.abooc.util.Debug;
import com.huawei.android.pushagent.PushEventReceiver;

public class HuaWeiReceiver extends PushEventReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Debug.error(intent.toString() + "\n"
                + intent.getExtras().toString());

        XiaoMiReceiver.handleIntent(context, intent);
    }

}
