package com.abooc.im.unittest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.abooc.im.R;

public class TestAnimActivity extends AppCompatActivity {


    public static void launch(Context context) {
        Intent intent = new Intent(context, TestAnimActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_anim);

        mXText = (TextView) findViewById(R.id.X);
        mXText.setVisibility(View.INVISIBLE);
        mAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_out);

    }

    Animation mAnimation;
    TextView mXText;

    public void onAnim(View view) {
        int index = (int) (Math.random() * 10);
        mXText.setText("X " + index);
        mXText.startAnimation(mAnimation);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
