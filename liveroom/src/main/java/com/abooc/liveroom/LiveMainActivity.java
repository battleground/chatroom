package com.abooc.liveroom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.abooc.util.Debug;
import com.bftv.fengmi.api.Live;
import com.bftv.fengmi.api.model.Package;
import com.bftv.fengmi.api.model.Publisher;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Debug.d("abcd");
        Debug.out("abcd");
        setContentView(R.layout.activity_live_main);
    }

    public void onStartEvent(View view) {
        LiveActivity.launch(this);
    }

    private void testApi() {
        Live.fm_live_create("10001live")
                .enqueue(new Callback<Package<Publisher>>() {
                    @Override
                    public void onResponse(Call<Package<Publisher>> call, Response<Package<Publisher>> response) {
                        Debug.error(response);
                    }

                    @Override
                    public void onFailure(Call<Package<Publisher>> call, Throwable t) {
                        Debug.error(t);

                    }
                });
    }
}
