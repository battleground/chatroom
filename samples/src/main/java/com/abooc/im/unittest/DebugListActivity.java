package com.abooc.im.unittest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.abooc.im.AppApplication;
import com.abooc.im.LeanCloud;
import com.abooc.im.R;
import com.abooc.im.activity.LeanCloudIMActivity;
import com.abooc.plugin.about.AboutActivity;

public class DebugListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView mListView;

    public static void launch(Context context) {
        Intent intent = new Intent(context, DebugListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_debug);

        RadioGroup iRadioGroup = (RadioGroup) findViewById(R.id.RadioGroup);
        iRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                View checkedView = group.findViewById(checkedId);
                if (group.indexOfChild(checkedView) == 0) {
                    LeanCloud.PLATFORM = LeanCloud.PLATFORM_MOBILE;
                } else {
                    LeanCloud.PLATFORM = LeanCloud.PLATFORM_TV;
                }
            }
        });
        RadioButton radioButton = (RadioButton) iRadioGroup.getChildAt(LeanCloud.PLATFORM == LeanCloud.PLATFORM_MOBILE ? 0 : 1);
        radioButton.toggle();

        mListView = (ListView) findViewById(R.id.ListView);
        mListView.setOnItemClickListener(this);
        mListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"LC实时消息", "礼物动画", "对话框"}));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.menu_logout);
        if (item != null) {
            item.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                AboutActivity.launch(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                LeanCloudIMActivity.launch(this);
                break;
            case 1:
                TestAnimActivity.launch(this);
                break;
            case 2:
//                Intent intent = new Intent(getBaseContext(), CoreService.class);
//                startService(intent);
                AppApplication.alert(getBaseContext(), null).show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
