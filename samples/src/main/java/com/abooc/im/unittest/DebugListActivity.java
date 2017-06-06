package com.abooc.im.unittest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        mListView = (ListView) findViewById(R.id.ListView);
        mListView.setOnItemClickListener(this);
        mListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"LC实时消息", "礼物动画"}));
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
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
