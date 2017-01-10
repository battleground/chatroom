package com.abooc.im;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.abooc.joker.tab.Tab;
import com.abooc.joker.tab.TabManager;
import com.abooc.util.Debug;
import com.leancloud.im.chatroom.AVIMClientManager;
import com.leancloud.im.chatroom.R;

import static com.abooc.im.MainActivity.NAMES.ACCOUNT;
import static com.abooc.im.MainActivity.NAMES.HOME;

/**
 * Created by dayu on 2016/12/5.
 */

public class MainActivity extends AppCompatActivity {

    public static void launch(Context ctx) {
        Intent intent = new Intent(ctx, MainActivity.class);
//        intent.putExtra(Constants.MEMBER_ID, memberId);
        ctx.startActivity(intent);
    }

    TabManager iTabManager;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String memberId = AVIMClientManager.getInstance().getClientId();
        getSupportActionBar().setSubtitle(memberId + " - " + "在线");


        iTabManager = new TabManager(this, getSupportFragmentManager(), R.id.TabContent);
        iTabManager.setOnSwitchListener(onSwitchListener);
        iTabManager
                .add(iTabManager.build(HOME.name, HOME.cls))
                .add(iTabManager.build(ACCOUNT.name, ACCOUNT.cls));

        Fragment fragment = iTabManager.instance(iTabManager.getTabs().get(0));
        iTabManager.switchTo(null, fragment);
    }


    private TabManager.OnSwitchListener onSwitchListener = new TabManager.OnSwitchListener() {
        @Override
        public void onSwitched(Fragment from, Fragment to) {
//            NAMES name = NAMES.valueOf(to.getTag());
//            switch (name) {
//                case HOME:
//                    setTitle(name.name);
//                    break;
//                case SEARCH:
//                    setTitle(name.name);
//                    break;
//                case ACCOUNT:
//                    setTitle(name.name);
//                    break;
//            }
            Log.d("Debug", to.getTag());
        }
    };

    enum NAMES {
        HOME("首页", ChatRoomListFragment.class),
        ACCOUNT("个人", ChatJoinedFragment.class);

        String name;
        Class<? extends Fragment> cls;

        NAMES(String name, Class<? extends Fragment> cls) {
            this.name = name;
            this.cls = cls;
        }
    }

    public void onClickTab(View view) {
        Fragment fragment;
        switch (view.getId()) {
            case R.id.menu_home:
                fragment = iTabManager.instance(iTabManager.getTabs().get(0));
                iTabManager.switchTo(iTabManager.content, fragment);
                break;
            case R.id.menu_live:
                Tab tab = iTabManager.getTabs().get(1);
                fragment = iTabManager.instance(tab);
                iTabManager.switchTo(iTabManager.content, fragment);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            fragment.queryConversations();
//        }
    }

    @Override
    public void onBackPressed() {
        Debug.anchor();
        super.onBackPressed();
        finish();
    }
}
