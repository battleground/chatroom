package com.abooc.im;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.abooc.im.MultiUserChatActivity;
import com.abooc.util.Debug;
import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.leancloud.im.chatroom.AVIMClientManager;
import com.leancloud.im.chatroom.Constants;
import com.leancloud.im.chatroom.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有会话列表页（群组会话、系统会话、暂态会话）
 */
public class CurrentChatsFragment extends Fragment {

    TextView mEmptyView;
    ListView mListView;
    ListAdapter mListAdapter = new ListAdapter();

    public CurrentChatsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        memberId = getArguments().getString(Constants.MEMBER_ID);
//        memberId = AVIMClientManager.getInstance().getClientId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_joined_covs, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.Refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
                queryConversations();
            }
        });
        mEmptyView = (TextView) view.findViewById(R.id.Empty);
        mListView = (ListView) view.findViewById(R.id.ListView);
        mListView.setEmptyView(mEmptyView);
        mListView.setAdapter(mListAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AVIMConversation conversation = mListAdapter.getItem(i);

                Intent intent = new Intent(getActivity(), MultiUserChatActivity.class);
                intent.putExtra(Constants.CONVERSATION_ID, conversation.getConversationId());
                intent.putExtra(Constants.ACTIVITY_TITLE, conversation.getName());
                startActivity(intent);
            }
        });

        clear();
        queryConversations();
    }

    @Override
    public void onResume() {
        super.onResume();
        clear();
        queryConversations();
    }

    public void clear() {
//        mEmptyView.setText("Loading...");
        mListAdapter.clear();
    }

    /**
     * 查询回话列表
     */
    public void queryConversations() {
        AVIMClient client = AVIMClientManager.getInstance().getClient();
        AVIMConversationQuery query = client.getQuery();
        query.setQueryPolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findInBackground(new AVIMConversationQueryCallback() {
            @Override
            public void done(final List<AVIMConversation> convs, AVIMException e) {
                if (Debug.printStackTrace(e)) return;
                //convs就是获取到的conversation列表
                //注意：按每个对话的最后更新日期（收到最后一条消息的时间）倒序排列
//                final Object toJSON = JSONArray.toJSON(convs);
                String json = JSON.toJSONString(convs);
                Debug.anchor(json);

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        mListAdapter.update(convs);
                    }
                });
            }
        });
    }


    class ListAdapter extends BaseAdapter {

        List<AVIMConversation> convs = new ArrayList<AVIMConversation>();

        public void update(List<AVIMConversation> convs) {
            this.convs = convs;
            notifyDataSetChanged();
        }

        public void clear() {
            this.convs.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return convs.size();
        }

        @Override
        public AVIMConversation getItem(int i) {
            return convs.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_chatroom_list_item, viewGroup, false);
            }

            AVIMConversation conversation = getItem(i);

            TextView textId = (TextView) view.findViewById(R.id.chatroom_list_item_id);
            TextView textName = (TextView) view.findViewById(R.id.chatroom_list_item_name);

            int size = conversation.getMembers().size();
            textName.setText(conversation.getName());
            textId.setText(conversation.getConversationId() + "  在线成员：" + size);
            return view;
        }
    }

}
