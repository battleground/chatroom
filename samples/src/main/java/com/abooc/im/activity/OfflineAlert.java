package com.abooc.im.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.abooc.im.LeanCloud;
import com.abooc.im.R;


/**
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-5-22.
 */
public class OfflineAlert extends Activity {

    /**
     * 显示
     *
     * @param context
     */
    public static void show(Context context) {
        Intent intent = new Intent(context, OfflineAlert.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_offline_alert);

    }

    public void onDismiss(View view) {
        super.onBackPressed();
        LeanCloud.getInstance().logout();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }


}
