package com.abooc.im;

import com.google.gson.Gson;

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
//        String time = time();
        String time = formatTime(120 * 1000L, "HH:mm:ss");
        out(time);
    }


    String time() {
        long millis = System.currentTimeMillis();

        Date date = new Date(millis);
        return date.toString();
    }


    /**
     * 00:00:00
     */
    public static String formatTime(Long ms, String format) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if (day > 0) {
            sb.append(day + "天");
        }
        if (hour > 0) {
            sb.append(fix(hour));
        } else {
            sb.append("00");
        }
        if (minute > 0) {
            sb.append(":" + fix(minute));
        } else {
            sb.append(":00");
        }
        if (second > 0) {
            sb.append(":" + fix(second));
        } else {
            sb.append(":00");
        }
        if (milliSecond > 0) {
            sb.append(":" + fix(milliSecond) + "毫秒");
        }
        return sb.toString();
    }

    static String fix(long i) {
        if (i < 10) {
            return "0" + i;
        } else {
            return String.valueOf(i);
        }
    }


    /**
     * 毫秒转化时分秒毫秒
     */
    public static String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if (day > 0) {
            sb.append(day + "天");
        }
        if (hour > 0) {
            sb.append(hour + ":");
        }
        if (minute > 0) {
            sb.append(minute + ":");
        }
        if (second > 0) {
            sb.append(second);
        }
        if (milliSecond > 0) {
            sb.append(":" + milliSecond + "毫秒");
        }
        return sb.toString();
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


    @Test
    public void test_page() throws Exception {

        int number = 32;
        int pageSize = 10;

        out("10: " + getPage(number, pageSize));
        out("20: " + getPage(47, 20));
        out("3: " + getPage(7, 3));

    }

    static int getPage(int total, int pageSize) {
        return ((total + (pageSize - 1)) / pageSize);
    }


    @Test
    public void test_number_format() throws Exception {
        int number = 3200300; // 3,200,300
        int length = String.valueOf(number).length();

        StringBuffer buffer = new StringBuffer();
        for (int levelLength = 1; levelLength <= length; levelLength++) {
            int level = (int) Math.pow(10f, length - levelLength);
            int v = number / (level);
            number = number % (level);
            buffer.append(v);

            if (length - levelLength > 0 && (length - levelLength) % 3 == 0) {
                buffer.append(",");
            }

            out(String.valueOf(v) + ", level:" + level + ", " + number);
        }
        String string = buffer.toString();

        out("\n" + string);

    }


    @Test
    public void test_number_format2() throws Exception {
        int number = 3200300; // 3,200,300
        out(number + ": " + toFormat(number));

        int number2 = 200300; // 200,300， 避免错误：",200,300"
        out(number + ": " + toFormat(number2));

        int number3 = 1234002422; // 1,234,002,422
        out(number + ": " + toFormat(number3));

        int number4 = 300; // 300， 避免错误：",300"
        out(number + ": " + toFormat(number4));


    }


    static String toFormat(int number) {
        String value = String.valueOf(number);
        int length = value.length();

        StringBuffer buffer = new StringBuffer(value);
        for (int i = 1; i <= ((length - 1) / 3); i++) {
            buffer.insert((length - i * 3), ',');
        }
        return buffer.toString();
    }

}