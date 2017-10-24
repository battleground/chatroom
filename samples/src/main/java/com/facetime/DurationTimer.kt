package com.facetime;

import android.os.Handler
import android.os.Message
import com.abooc.util.Debug
import com.facetime.mvp.FaceTimeViewer

/**
 * 通话时长
 */
class DurationTimer(val viewer: FaceTimeViewer) : Handler() {

    var time = 0L
    var run = true


    fun start() {
        run = true
        sendEmptyMessage(1)
    }

    override fun handleMessage(msg: Message) {
        Debug.error("$run " + time)
        if (run) {
            val formatTime = formatTime(time * 1000, "HH:mm:ss")
            if (msg.what == 1) {
                time++
                viewer.onTick(time, formatTime)
                sendEmptyMessageDelayed(1, 998)
            }
        }
    }

    fun stop() {
        run = false
        removeMessages(1)
    }

    /**
     * 00:00:00
     */
    fun formatTime(ms: Long?, format: String): String {
        val ss = 1000
        val mi = ss * 60
        val hh = mi * 60
        val dd = hh * 24

        val day = ms!! / dd
        val hour = (ms - day * dd) / hh
        val minute = (ms - day * dd - hour * hh) / mi
        val second = (ms - day * dd - hour * hh - minute * mi) / ss
        val milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss

        val sb = StringBuffer()
        if (day > 0) {
            sb.append(day.toString() + "天")
        }
        if (hour > 0) {
            sb.append(fix(hour))
        } else {
            sb.append("00")
        }
        if (minute > 0) {
            sb.append(":" + fix(minute))
        } else {
            sb.append(":00")
        }
        if (second > 0) {
            sb.append(":" + fix(second))
        } else {
            sb.append(":00")
        }
        if (milliSecond > 0) {
            sb.append(":" + fix(milliSecond) + "毫秒")
        }
        return sb.toString()
    }

    internal fun fix(i: Long): String {
        if (i < 10) {
            return "0" + i
        } else {
            return i.toString()
        }
    }
}