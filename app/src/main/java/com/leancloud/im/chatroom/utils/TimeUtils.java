package com.leancloud.im.chatroom.utils;

import java.util.TimeZone;

/**
 * @author zhangjunpu
 * @date 2016/12/8
 */

public class TimeUtils {

    public static final int SECONDS_IN_DAY = 60 * 60 * 24;
    public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

    /**
     * 判断两个时间戳是否为同一天
     * @param ms1 毫秒
     * @param ms2 毫秒
     * @return true or false
     */
    public static boolean isSameDayOfMillis(final long ms1, final long ms2) {
        final long interval = ms1 - ms2;
        return interval < MILLIS_IN_DAY
                && interval > -1L * MILLIS_IN_DAY
                && toDay(ms1) == toDay(ms2);
    }

    private static long toDay(long millis) {
        return (millis + TimeZone.getDefault().getOffset(millis)) / MILLIS_IN_DAY;
    }

}
