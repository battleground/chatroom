package com.facetime

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.PowerManager
import android.support.v4.app.TaskStackBuilder
import android.support.v7.app.NotificationCompat
import android.view.View
import android.view.WindowManager
import com.abooc.im.R
import com.abooc.im.message.CallMessage
import com.abooc.util.Debug
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
class FaceTimeActivity : Activity(), FaceTimeViewer {

    val FaceTime = FaceTimePresenter(this)
    val Ring = Ring(this)
    var durationTimer = DurationTimer(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)

        setContentView(R.layout.activity_face_time)
        val action = intent.getIntExtra(INTENT_KEY_ACTION, CallMessage.ACTION_HANG_UP)
        if (action == CallMessage.ACTION_HANG_UP) {
            holdOn.visibility = View.GONE
        }

        val who = intent.getStringExtra(INTENT_KEY_UID)
        FaceTime.uid = who
        uidText.text = "+$who"

        FaceTime.onCreate()
        Ring.load()

        keepScreenOn(this, true)
        openKeyguard()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onTick(seconds: Long, time: String) {
        timeText.text = time
    }

    override fun onNoAnswer() {
        stopCall()

        CallOutActivity.show(applicationContext)
        this@FaceTimeActivity.finish()
    }

    var onHoldOnEvent: ((View) -> Unit) = {}
    var onHungUpEvent: (() -> Unit) = {}

    fun onHoldOn(view: View) {
        onHoldOnEvent.invoke(view)
        onUIHoldOn()
    }

    fun onHungUp(view: View) {
        onHungUpEvent.invoke()
        onUIHungUp()
    }

    override fun onUIHoldOn() {
        FaceTime.holdOn()
        holdOn.visibility = View.GONE
        durationTimer.start()
        Ring.stop()
    }

    override fun onUIHungUp() {
        FaceTime.hungUp()
        stopCall()
    }

    fun stopCall() {
        hungUp.isEnabled = false
        holdOn.isEnabled = false
        durationTimer.stop()
        timeText.text = "结束通话"
        Handler().postDelayed({ finish() }, 1000)
    }


    override fun onBackPressed() {

    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
    }

    override fun onDestroy() {
        Debug.error()
        super.onDestroy()
//        onHungUpEvent.invoke()
//        onUIHungUp()

        Ring.stop()
        keepScreenOn(this, false)
        FaceTime.destroy()
    }


    fun keepScreenOn(context: Context, on: Boolean) {
        var pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "FaceTimeActivity.KeepScreenOn")
        wakeLock.setReferenceCounted(false)
        if (wakeLock != null)
            if (on) {
                wakeLock.acquire()
            } else {
                wakeLock.release()
            }
    }

    private fun openKeyguard() {
        window.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        try {
            //打开锁屏
            val keyguardManager = applicationContext.getSystemService(KEYGUARD_SERVICE) as KeyguardManager
            val keyguardLock = keyguardManager.newKeyguardLock("unLock")
            //解锁
            keyguardLock.disableKeyguard()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    companion object {

        val INTENT_KEY_ACTION = "action"
        val INTENT_KEY_UID = "uid"

        /**
         * 显示电话接听页面

         * @param ctx
         */
        fun show(ctx: Context, uid: String, action: Int) {
            val intent = Intent(ctx, FaceTimeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(INTENT_KEY_UID, uid)
            intent.putExtra(INTENT_KEY_ACTION, action)
            ctx.startActivity(intent)
        }
    }

    override fun onStart() {
        Debug.error("noti-cancel()")
        super.onStart()
        cancel()
    }

    override fun onStop() {
        super.onStop()
        if (FaceTime.isWaiting()
                || FaceTime.isConnecting()) {
            show()
        }
    }


    var idNotification = 0x111
    fun show() {
        Debug.error("noti-show()")
        val mBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_menu_call)
                .setContentTitle("正在通话...")
                .setContentText("与【${FaceTime.uid}】正在进行视频通话")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        // Creates an explicit intent for an Activity in your app
//        val resultIntent = Intent(this, FaceTimeActivity::class.java)
        intent.setClass(this, FaceTimeActivity::class.java)
        val resultIntent = intent

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        val stackBuilder = TaskStackBuilder.create(this)
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(FaceTimeActivity::class.java)
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent)
        val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        mBuilder.setContentIntent(resultPendingIntent)
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = mBuilder.build()
        notification.flags = Notification.FLAG_AUTO_CANCEL or Notification.FLAG_NO_CLEAR
//        notification.sound = Uri.parse("android_asset:///phonering.mp3")
        // mId allows you to update the notification later on.
        mNotificationManager.notify(idNotification, notification)
    }

    fun cancel() {
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.cancel(idNotification)
    }

}
