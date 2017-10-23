package com.abooc.im;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.abooc.util.Debug;
import com.avos.avoscloud.AVMixpushManager;

/**
 * Created by dayu on 2017/10/12.
 */

public class LcConfig {


    //风迷【测试】环境的
//    public final static String LEANCLOUD_APP_ID  = "IagLpuUNFcJeb3W07nVzsNuH-gzGzoHsz";
//    public final static  String LEANCLOUD_APP_KEY = "fHKkj3Y2vtD2XCOidhTXDyCu";


    //风迷【正式】环境的
    public final static String LEANCLOUD_APP_ID = "OgURl0JWWtADk1r5RcEgQ6TL-gzGzoHsz";
    public final static String LEANCLOUD_APP_KEY = "IUf49DibYiaCkClsheVWGWXa";

    // "直播间聊天系统" 测试key
//    public final static String LEANCLOUD_APP_ID = "Ly7xoqX3c4P8hn7pYQoDaxXv-gzGzoHsz";
//    public final static String LEANCLOUD_APP_KEY = "KjaBxRRx7H6udI3VMRGde4tl";
    public final static String CONVERSATION_ID = "58411255128fe1005898c163"; // 005
    public final static String CONVERSATION_ID_2 = "583ff241ac502e006cbc626f"; // 006

    //    public final static String CONVERSATION_ID_TOM_JERRY = "592fbc5c1b69e6005ca9c156"; // 聊天会话
//    public final static String CONVERSATION_ID_TOM_JERRY = "58468c7861ff4b006ba812a9"; // 辛小新 的频道
    public final static String CONVERSATION_ID_TOM_JERRY_SYSTEM = "5874e1212f301e006be81720"; // 通知会话

    public static String CONVERSATION_ID_TEMP = "59df5809128fe100377b7915"; // Tom & Jerry会话

    // test
//    public final static String LEANCLOUD_APP_ID  = "p96jQI9whtwV57DptXlMBEWj-gzGzoHsz";
//    public final static  String LEANCLOUD_APP_KEY = "9hVWh7D8Fxq4vxnuh4zKC9f8";
//    public final static String CONVERSATION_ID = "58aad41761ff4b006b59dce0";
//    public final static String CONVERSATION_ID_2 = "58aad415570c35006b5517b4"; // 电影纵贯线
//    public final static String CONVERSATION_ID_2 = "589aab5861ff4b0058dc30d3"; // 大胃王密子君会话


    public static void xPush(Context context){
//        AVMixpushManager.registerXiaomiPush(context, "2882303761517624249", "5491762483249", "call_in");
//        AVMixpushManager.registerFlymePush(context, "111435", "37476741a4b14796b649252e4b8c020c", "call_in");
        AVMixpushManager.registerHuaweiPush(context, "im");
        String Imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
                .getDeviceId();
        Debug.error(Imei);
    }

}
