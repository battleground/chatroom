package com.abooc.im.notifications;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abooc.im.R;
import com.abooc.joker.adapter.recyclerview.RecyclerViewAdapter;
import com.abooc.joker.adapter.recyclerview.ViewHolder;
import com.abooc.util.Debug;
import com.abooc.widget.Toast;

import java.util.Arrays;
import java.util.List;


/**
 */
public class NotificationsFragment extends Fragment implements MVP.Viewer, ViewHolder.OnRecyclerItemChildClickListener
, ViewHolder.OnRecyclerItemClickListener{

    MVP.Notifications iNotifications;

    public NotificationsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView iRecyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        iRecyclerView.setLayoutManager(manager);
        Adapter iAdapter = new Adapter();


        String[] a = {
                "abc", "abc", "abc", "abc",
                "abc", "abc", "abc", "abc",
                "abc", "abc", "abc", "abc"
        };
        List<String> stringList = Arrays.asList(a);
        iAdapter.getCollection().update(stringList);
        iRecyclerView.setAdapter(iAdapter);


        MVP.Viewer iViewer = null;
        iNotifications = new NotificationsPresenter(iViewer);
//        iNotifications.load();
    }

    @Override
    public void onItemChildClick(RecyclerView recyclerView, View itemView, View clickView, int position) {

        Debug.anchor("position:" + position);
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {

        Debug.anchor("position:" + position);
    }

    @Override
    public void showList(List<?> list) {

    }


    private class Adapter extends RecyclerViewAdapter<String> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notifications_list_item, parent, false);
            return new Holder(v, NotificationsFragment.this, NotificationsFragment.this);
        }

        @Override
        public void onBindViewHolder(ViewHolder h, int position) {
            String bean = getItem(position);
            Holder holder = (Holder) h;

        }
    }

    private class Holder extends ViewHolder {

        public Holder(View itemLayoutView, OnRecyclerItemClickListener listener) {
            super(itemLayoutView, listener);
        }

        public Holder(View itemLayoutView, OnRecyclerItemClickListener listener, OnRecyclerItemChildClickListener childListener) {
            super(itemLayoutView, listener, childListener);
        }

        @Override
        public void onBindedView(View itemLayoutView) {
            itemLayoutView.findViewById(R.id.content).setOnClickListener(this);

        }
    }
}
