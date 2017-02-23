package com.abooc.im;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.junit.Test;

import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    void out(String message) {
        System.out.println(message);
    }

    @Test
    public void test_String_format() throws Exception {
        String string = String.format("加入会话(%s)", "aaa");
        out(String.format("加入会话(%s)", "aaa"));
        out(string);
    }

    @Test
    public void addition_isCorrect() throws Exception {
        String time = time();
        out(time);
    }


    String time() {
        long millis = System.currentTimeMillis();

        Date date = new Date(millis);
        return date.toString();
    }


    @Test
    public void test_json() throws Exception {
        String json = "{\n" +
                "        \"alert\":             \"消息内容\",\n" +
                "        \"title\":             \"显示在通知栏的标题\",\n" +
                "        \"silent\":            false,\n" +
                "        \"payload\": { \n" +
                "            \"type\": \"1\", \n" +
                "            \"model\": { \n" +
                "                \"id\": \"9\",\n" +
                "                \"carname\": \"手工生活\"\n" +
                "            }\n" +
                "        }\n" +
                "    }";

        Gson gson = new Gson();
        NotifyMessage notifyMessage = gson.fromJson(json, NotifyMessage.class);
        out(notifyMessage.alert);
        out(notifyMessage.payload.type);
        out(notifyMessage.payload.model.toString());

    }


}