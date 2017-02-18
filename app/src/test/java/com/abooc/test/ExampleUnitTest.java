package com.abooc.test;

import com.abooc.test.data.User;
import com.abooc.test.data.UserLoader;
import com.abooc.util.Debug;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        User user = new User("001", "老张", null);
        Object json = JSON.toJSON(user);
        Debug.out(json);


        UserLoader.loadUsers(new UserLoader.OnGetUsersListener() {
            @Override
            public void onGet(ArrayList<User> users) {

                Object toJSON = JSONArray.toJSON(users);
                Debug.out(toJSON);
            }
        });

    }


    @Test
    public void test_lc(){

    }

}