package com.abooc.im;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abooc.joker.adapter.recyclerview.ViewHolder.OnRecyclerItemClickListener;
import com.abooc.test.data.LiveRoom;
import com.abooc.util.Debug;
import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.leancloud.im.chatroom.AVIMClientManager;
import com.leancloud.im.chatroom.R;
import com.leancloud.im.chatroom.adapter.LiveRoomAdapter;
import com.leancloud.im.chatroom.utils.AssetsUtils;

import java.util.List;

/**
 * 所有会话列表页（群组会话、系统会话、暂态会话）
 */
public class AllChatsFragment extends Fragment implements OnRecyclerItemClickListener {


    //    String memberId;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRoomList;
    private LiveRoomAdapter mAdapter;

    public AllChatsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        memberId = getArguments().getString(Constants.MEMBER_ID);
//        memberId = AVIMClientManager.getInstance().getClientId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chatroom_list, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                        getLiveRooms();
                    }
                }, 800);
            }
        });
        mRoomList = (RecyclerView) view.findViewById(R.id.roomList);
        mRoomList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new LiveRoomAdapter(getContext());
        mAdapter.setOnRecyclerItemClickListener(this);
        mRoomList.setAdapter(mAdapter);

        clear();
        getLiveRooms();
    }

    @Override
    public void onResume() {
        super.onResume();
        clear();
    }

    /**
     * 获取直播间列表
     */
    public void getLiveRooms() {
        String json = AssetsUtils.getFromAssets("LiveRoomBackup.json", getContext());
        List<LiveRoom> list = JSON.parseArray(json, LiveRoom.class);
        mAdapter.update(list);
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
        LiveRoom room = mAdapter.getItem(position);
        if (room == null) return;
        AVIMClient client = AVIMClientManager.getInstance().getClient();
        AVIMConversation conversation = client.getConversation(room.getConversationId());
        conversation.join(new AVIMConversationCallback() {
            @Override
            public void done(AVIMException e) {
                if (e == null) {

                    Debug.anchor("加入会话：成功！");
                } else {
                    Debug.error("加入会话：" + e);
                }
            }
        });

        MultiUserChatActivity.launch(getActivity(), room.getConversationId(), room.getTitle());
    }


    public void clear() {
//        mEmptyView.setText("Loading...");
//        mListAdapter.clear();
    }

}
