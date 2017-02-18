package com.abooc.im;

import android.app.Activity;

import com.abooc.plugin.about.About;
import com.abooc.plugin.about.UpdateActivity;
import com.abooc.plugin.about.Updater;
import com.abooc.plugin.about.Version;
import com.abooc.util.Debug;
import com.google.gson.Gson;

import im.fir.sdk.FIR;
import im.fir.sdk.VersionCheckCallback;

/**
 * Created by dayu on 2017/2/18.
 */

public class Update {

    public static void checkForUpdate(final Activity activity) {
        FIR.checkForUpdateInFIR("a6d6a4ddfbfffaa80d949485d397447a", new VersionCheckCallback() {
            @Override
            public void onSuccess(String versionJson) {
                About about = About.getAbout();
//                about.setUpdateUrl("http://fir.im/fmpd");

                Gson gson = new Gson();
                Version version = gson.fromJson(versionJson, Version.class);
                about.setUpdateUrl(version.install_url);
                int versionCode = About.getVersionCode(activity);
                if (versionCode < Integer.valueOf(version.version)) {
                    Updater.setBackEnable(true);
                    Updater.launch(activity);
                }

            }

            @Override
            public void onFail(Exception exception) {
                Debug.anchor("检查更新失败! " + "\n" + exception.getMessage());
            }

            @Override
            public void onStart() {
                Debug.anchor("正在检查更新...");
            }

            @Override
            public void onFinish() {
                Debug.anchor("检查更新完成");
            }
        });
    }
}
