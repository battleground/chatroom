package com.facetime;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.abooc.im.CoreService;
import com.abooc.im.LeanCloud;
import com.abooc.im.R;
import com.abooc.im.Saver;
import com.abooc.im.activity.LoginActivity;


/**
 * 显示电话接听页面
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-5-22.
 */
public class X extends Activity {

    /**
     * 显示电话接听页面
     *
     * @param ctx
     */
    public static void show(Context ctx) {
        Intent intent = new Intent(ctx, X.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        setContentView(R.layout.activity_x);

        CoreService.launch(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String uid = Saver.read(X.this);

                if (TextUtils.isEmpty(uid)) {
                    LoginActivity.launch(X.this);
                } else {
                    LeanCloud.getInstance().createClient(uid);
                    CallOutActivity.Companion.show(X.this);
                }

                finish();
            }
        }, 1000);

    }

    @Override
    protected void onResume() {
        super.onResume();

//        clear();
    }

    void clear() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
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
