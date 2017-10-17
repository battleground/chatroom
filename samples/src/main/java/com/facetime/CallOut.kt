package com.facetime

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import com.abooc.im.LeanCloud
import com.abooc.im.R
import com.abooc.util.Debug
import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMClientEventHandler
import com.avos.avoscloud.im.v2.AVIMClientOpenOption
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback
import com.facetime.mvp.CallOutPresenter
import com.facetime.mvp.Viewer
import kotlinx.android.synthetic.main.activity_face_time_call_out.*


/**
 * 拨打电话页面
 * Created by author:李瑞宇
 * email:allnet@live.cn
 * on 15-5-22.
 */
class CallOut : AppCompatActivity(), Viewer {

    val AVIMClientEventHandler = object : AVIMClientEventHandler() {
        override fun onConnectionResume(avimClient: AVIMClient) {
            title = avimClient.clientId + " - 在线"
        }

        override fun onConnectionPaused(avimClient: AVIMClient) {
            title = avimClient.clientId + " - 离线"
        }

        override fun onClientOffline(avimClient: AVIMClient, p1: Int) {
            title = avimClient.clientId + " - 离线"
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)

        setContentView(R.layout.activity_face_time_call_out)
        val clientId = LeanCloud.getInstance().clientId
        title = clientId + " - 在线"
        supportActionBar!!.subtitle = "平台：" + LeanCloud.PLATFORM

        CallOutPresenter(this)

        val leanCloud = LeanCloud.getInstance()
        leanCloud.addAVIMClientEventHandler(AVIMClientEventHandler)
        leanCloud.addLogoutListener { finish() }

        uid.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                val b = s.length > 0
                call.isEnabled = b
                queryStatus.isEnabled = b
                pushNotification.isEnabled = b
                pushSilent.isEnabled = b
            }

        })
        uid.text = null
    }

    fun getPhone(): String {
        return "86 ${uid.text}"
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN && Keyboard.hideKeyboard(this)) {
            return true
        } else
            return super.dispatchTouchEvent(ev)
    }

    fun online() {
        messageInfo.text = "${uid.text}：在线"
    }

    fun offline() {
        messageInfo.text = "${uid.text}：离线"
    }

    fun error(error: String) {
        messageInfo.text = error
    }

    var onCallEvent: ((View) -> Unit) = {}
    var onPushCallback: ((View) -> Unit) = {}

    /**
     * 上线
     */
    fun onOpen(view: View) {
        val clientId = LeanCloud.getInstance().clientId
        val avimClient = LeanCloud.getInstance().createClient(clientId)
        val openOption = AVIMClientOpenOption()
        openOption.isForceSingleLogin = true
        avimClient.open(openOption, object : AVIMClientCallback() {
            override fun done(avimClient: AVIMClient, e: AVIMException?) {
                if (e == null) {
                    messageInfo.text = "在线"
                } else {
                    Debug.error("上线失败：$e")
                }
            }
        })
    }

    /**
     * 显示来电页面
     */
    fun onCallIn(view: View) {
        CallIn.show(this)
    }

    /**
     * 拨打
     */
    fun onCallPhone(view: View) {
        onCallEvent.invoke(view)

        CallIn.show(this)
    }

    /**
     * 推送通知栏消息
     */
    fun onPush(view: View) {
        onPushCallback.invoke(view)
    }

    var onQueryEvent: ((View) -> Unit) = {}
    /**
     * 查询用户在线状态
     */
    fun onQueryOnline(view: View) {
        scrollView.smoothScrollTo(0, 0)
        onQueryEvent.invoke(view)
    }

    var onExitEvent: ((View) -> Unit) = {}
    /**
     * 用户退出
     */
    fun onExit(view: View) {
        onExitEvent.invoke(view)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
    }

    override fun onDestroy() {
        super.onDestroy()
        val leanCloud = LeanCloud.getInstance()
        leanCloud.removeAVIMClientEventHandler(AVIMClientEventHandler)
        leanCloud.removeLogoutListener(null)
    }

    companion object {

        /**
         * 拨打电话页面

         * @param ctx
         */
        fun show(ctx: Context) {
            val intent = Intent(ctx, CallOut::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            ctx.startActivity(intent)
        }
    }

}
