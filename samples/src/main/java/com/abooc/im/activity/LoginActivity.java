package com.abooc.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.abooc.im.LeanCloud;
import com.abooc.im.R;
import com.abooc.im.Saver;
import com.abooc.im.unittest.DebugListActivity;
import com.abooc.util.Debug;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMClientOpenOption;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.facetime.CallOut;
import com.facetime.Keyboard;

public class LoginActivity extends AppCompatActivity {


    private TextView mMessageText;
    private EditText mEditText;
    private boolean logining;
    private String clientId;


    public static final String LEANCLOUND_CLIENT_TOM = "Tom";
    public static final String LEANCLOUND_CLIENT_JERRY = "Jerry";


    public static void launch(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mMessageText = (TextView) findViewById(R.id.log);
        mEditText = (EditText) findViewById(R.id.uid);

        final RadioGroup iRadioGroup = (RadioGroup) findViewById(R.id.RadioGroup);
        RadioButton radioButton = (RadioButton) iRadioGroup.getChildAt(0);
        iRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                View checkedView = group.findViewById(checkedId);

                switch (group.indexOfChild(checkedView)) {
                    case 0:
                        clientId = LEANCLOUND_CLIENT_TOM;
                        mEditText.setText(null);
                        break;
                    case 1:
                        clientId = LEANCLOUND_CLIENT_JERRY;
                        mEditText.setText(null);
                        break;
                }
            }
        });
//        radioButton.toggle();

        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    iRadioGroup.clearCheck();
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && Keyboard.hideKeyboard(this)) {
            return true;
        } else
            return super.dispatchTouchEvent(ev);
    }

    public void onLogin(final View view) {
        mMessageText.setText("loading...");
        mMessageText.setVisibility(View.VISIBLE);
        logining = true;
        view.setEnabled(false);


        int length = mEditText.getText().length();
        if (length > 0) {
            clientId = mEditText.getText().toString().trim().toLowerCase();
        }


        AVIMClient avimClient = LeanCloud.getInstance().createClient(clientId);
        AVIMClientOpenOption openOption = new AVIMClientOpenOption();
        openOption.setForceSingleLogin(true);
        avimClient.open(openOption, new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (e == null) {
                    Saver.save(LoginActivity.this, clientId);

                    Debug.anchor(clientId + "【登录】成功");
                    mMessageText.setText("登录成功");
                    mMessageText.setVisibility(View.VISIBLE);

                    LeanCloud.getInstance().online(true);

                    new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
//                            GiftSamplesActivity.launch(getBaseContext());
                            CallOut.Companion.show(getBaseContext());
                            finish();
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }
                    }.sendEmptyMessageDelayed(0, 1000);
                } else {
                    Debug.error(clientId + "【登录】失败, " + e);
                    view.setEnabled(true);
                    mMessageText.setText("登录失败");
                    mMessageText.setVisibility(View.VISIBLE);

                    new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            logining = false;
                            mMessageText.setVisibility(View.INVISIBLE);
                        }
                    }.sendEmptyMessageDelayed(0, 2000);
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

    public void onOpenDebug(View view) {
        DebugListActivity.launch(this);
    }

}
