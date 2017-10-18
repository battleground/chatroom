package com.facetime

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.PowerManager
import android.view.View
import com.abooc.im.R
import com.abooc.im.message.CallMessage
import com.facetime.mvp.FaceTimePresenter
import com.facetime.mvp.FaceTimeViewer
import com.facetime.mvp.Ring
import kotlinx.android.synthetic.main.activity_face_time.*


/**
 * 显示电话接听页面
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-5-22.
 */
class FaceTime : Activity(), FaceTimeViewer {

    inner class TimerDown : Handler() {
        override fun handleMessage(msg: Message) {
            if (!FaceTime.isConnected) {
                this@FaceTime.finish()
            }
        }
    }

    val iTimerDown = TimerDown()

    val FaceTime = FaceTimePresenter(this)
    val Ring = Ring(this)
    var uiTimer = UITimer(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)

        setContentView(R.layout.activity_face_time)
        val action = intent.getIntExtra("action", CallMessage.ACTION_HANG_UP)
        if (action == CallMessage.ACTION_HANG_UP) {
            holdOn.visibility = View.GONE
        }

        FaceTime.onCreate()

        val who = intent.getStringExtra("uid")
        FaceTime.uid = who
        uidText.text = "+$who"

        Ring.load()
        iTimerDown.sendEmptyMessageDelayed(0, (60 * 1000).toLong())
    }

    override fun onResume() {
        super.onResume()

        keepScreenOn(this, true)
    }

    override fun onTick(seconds: Long, time: String) {
        timeText.text = time
    }

    var onHoldOnEvent: ((View) -> Unit) = {}
    var onHungUpEvent: ((View) -> Unit) = {}

    fun onHoldOn(view: View) {
        onHoldOnEvent.invoke(view)
        onUIHoldOn()
    }

    fun onHungUp(view: View) {
        onHungUpEvent.invoke(view)
        onUIHungUp()
    }

    override fun onUIHoldOn() {
        FaceTime.isConnected = true
        holdOn.visibility = View.GONE
        uiTimer.start()
        Ring.stop()
    }

    override fun onUIHungUp() {
        FaceTime.isConnected = false
        hungUp.isEnabled = false
        holdOn.isEnabled = false
        uiTimer.stop()
        timeText.text = "结束通话"
        Handler().postDelayed({ finish() }, 1000)
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
        super.onDestroy()
        Ring.stop()
        keepScreenOn(this, false)
        uiTimer.stop()
        FaceTime.destroy()
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
        fun show(ctx: Context, uid: String, action: Int) {
            val intent = Intent(ctx, FaceTime::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("uid", uid)
            intent.putExtra("action", action)
            ctx.startActivity(intent)
        }
    }

}
