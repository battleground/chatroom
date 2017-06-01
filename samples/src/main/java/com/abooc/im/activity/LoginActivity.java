package com.abooc.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.abooc.im.AppApplication;
import com.abooc.im.R;
import com.abooc.util.Debug;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

public class LoginActivity extends AppCompatActivity {


    private TextView mMessageText;
    private AVIMClient mClient;
    private boolean logining;


    public static final String LEANCOUND_CLIENT_TOM = "Tom";
    public static final String LEANCOUND_CLIENT_JERRY = "Jerry";


    public static void launch(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mMessageText = (TextView) findViewById(R.id.log);

        RadioGroup iRadioGroup = (RadioGroup) findViewById(R.id.RadioGroup);
        RadioButton radioButton = (RadioButton) iRadioGroup.getChildAt(0);
        iRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                View checkedView = group.findViewById(checkedId);
                if (group.indexOfChild(checkedView) == 0) {
                    AppApplication.LC_CLIENT = LEANCOUND_CLIENT_TOM;
                    mClient = AVIMClient.getInstance(LEANCOUND_CLIENT_TOM);
                } else {
                    AppApplication.LC_CLIENT = LEANCOUND_CLIENT_JERRY;
                    mClient = AVIMClient.getInstance(LEANCOUND_CLIENT_JERRY);
                }
            }
        });
        radioButton.toggle();

    }

    public void onLogin(final View view) {
        logining = true;
        view.setEnabled(false);
        mClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (e == null) {
                    mMessageText.setText("登录成功");

                    new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            GiftSamplesActivity.launch(getBaseContext());
                            finish();
                        }
                    }.sendEmptyMessageDelayed(0, 1000);
                } else {
                    view.setEnabled(true);
                    mMessageText.setText("登录失败");
                    Debug.error(AppApplication.LC_CLIENT + " 登录失败！" + e);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!logining)
            super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
