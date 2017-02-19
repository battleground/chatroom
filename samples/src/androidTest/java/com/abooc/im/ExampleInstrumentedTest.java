package com.abooc.im;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.abooc.util.Debug;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.abooc.chatroom", appContext.getPackageName());
    }

    @Test
    public void test_login() throws Exception {
        Log.d("TAG", "test_login()");
        Debug.enable();
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.abooc.chatroom", appContext.getPackageName());

        Debug.anchor(appContext.getPackageName());


        AVIMClient tom = AVIMClient.getInstance("Tom");
        tom.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {

                assertNotEquals(e, null);

                if (e == null) {
//
//                    AVIMClient mClient = AVIMClient.getInstance("Tom");
//                    Debug.anchor(mClient + " 登录成功！");
//
////                    AVIMTextMessage msg = new AVIMTextMessage();
////                    msg.setText("来自 Tom 的系统消息");
////                    Map<String, Object> attributes = new HashMap<String, Object>();
////                    attributes.put("location", "拉萨布达拉宫");
////                    attributes.put("Title", "这蓝天……我彻底是醉了");
////                    msg.setAttrs(attributes);
////                    client.getConversation("5836b565da2f600062854730").sendMessage(msg,
////                            new AVIMConversationCallback() {
////                                @Override
////                                public void done(AVIMException e) {
////                                    if (e == null) {
////                                        // 发送成功
////                                    }
////                                }
////                            });
                }
            }
        });
    }
}
