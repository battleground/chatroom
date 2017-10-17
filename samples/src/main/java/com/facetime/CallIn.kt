package com.facetime

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.PowerManager
import com.abooc.im.CoreService

import com.abooc.im.R
import com.abooc.util.Debug
import com.facetime.mvp.Ring


/**
 * 显示电话接听页面
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-5-22.
 */
class CallIn : Activity() {


    val Ring = Ring(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        Debug.error()
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)

        setContentView(R.layout.activity_face_time_call_in)

        object : Handler() {
            override fun handleMessage(msg: Message) {
                this@CallIn.finish()
            }
        }.sendEmptyMessageDelayed(0, (5 * 1000).toLong())

        Ring.load()
    }

    override fun onResume() {
        super.onResume()

        keepScreenOn(this, true)

    }


    fun keepScreenOn(context: Context, on: Boolean) {
        var pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "FaceTime.KeepScreenOn")
        wakeLock.setReferenceCounted(false)
        if (wakeLock != null)
            if (on) {
                wakeLock.acquire()
            } else {
                wakeLock.release()
            }
    }

    override fun onBackPressed() {

    }

    override fun onDestroy() {
        Debug.error()
        super.onDestroy()
        Ring.stop()
        keepScreenOn(this, false)
    }

    override fun finish() {
        super.finish()
        CallOut.show(this)
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
    }

    companion object {

        /**
         * 显示电话接听页面

         * @param ctx
         */
        fun show(ctx: Context) {
            val intent = Intent(ctx, CallIn::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            ctx.startActivity(intent)
        }
    }

}
