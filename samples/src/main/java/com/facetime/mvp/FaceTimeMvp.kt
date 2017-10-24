package com.facetime.mvp


import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import android.os.Handler
import android.os.Message
import com.abooc.im.LeanCloud
import com.abooc.im.R
import com.abooc.im.message.CallMessage
import com.abooc.im.message.CallMessage.ACTION_HANG_UP
import com.abooc.im.message.CallMessage.ACTION_HOLD_ON
import com.abooc.util.Debug
import com.abooc.widget.Toast
import com.avos.avoscloud.AVInstallation
import com.avos.avoscloud.AVPush
import com.avos.avoscloud.im.v2.*
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback
import com.avos.avoscloud.im.v2.callback.AVIMOnlineClientsCallback
import com.facetime.FaceTimeActivity
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import org.lee.java.util.Empty
import org.lee.java.util.ToString
import java.util.*

/**
 * Created by dayu on 2017/10/18.
 */

interface FaceTimeViewer {

    fun onUIHoldOn()
    fun onUIHungUp()
    fun onTick(seconds: Long, time: String)

    fun onNoAnswer()

}


class FaceTimePresenter(val viewer: FaceTimeViewer) {

    companion object {
        val CALL_CONNECTING = 1
        val CALL_WAITING = 2
        val CALL_NO_ANSWER = 3
        val CALL_HUNG_UP = 4

        val MESSAGE_HUNG_UP = 0
        val HUNG_UP_TIME_DELAY_MILLIS = 60 * 1000
    }

    inner class NoAnswerTimer : Handler() {

        override fun handleMessage(msg: Message) {
            Debug.error()
            viewer.onNoAnswer()
        }

    }

    val hungUpTimer = NoAnswerTimer()

    var uid: String? = null
    val mSender = Sender()
    var iReceiver: Receiver? = null

    var callStatus = CALL_WAITING

    init {
    }

    fun onCreate() {
        iReceiver = Receiver()
        iReceiver?.onCreate(viewer)

        val faceTimeViewer = viewer as FaceTimeActivity
        faceTimeViewer.onHoldOnEvent = {
            Debug.error(uid)
            mSender.holdOn(uid!!)
        }
        faceTimeViewer.onHungUpEvent = {
            Debug.error(uid)
            mSender.hungUp(uid!!)
        }
        hungUpTimer.sendEmptyMessageDelayed(MESSAGE_HUNG_UP, (HUNG_UP_TIME_DELAY_MILLIS).toLong())
    }

    /**
     * 呼叫等待接听中...
     */
    fun isWaiting(): Boolean {
        return callStatus == CALL_WAITING
    }

    /**
     * 通话中...
     */
    fun isConnecting(): Boolean {
        return callStatus == CALL_CONNECTING
    }

    fun hungUp() {
        callStatus = CALL_HUNG_UP
        hungUpTimer.removeMessages(MESSAGE_HUNG_UP)
    }

    fun holdOn() {
        callStatus = CALL_CONNECTING
        hungUpTimer.removeMessages(MESSAGE_HUNG_UP)
    }

    fun destroy() {
        callStatus = CALL_WAITING
        hungUpTimer.removeMessages(MESSAGE_HUNG_UP)
        iReceiver?.destroy()
    }

    class CustomMessageHandler(val viewer: FaceTimeViewer) : AVIMMessageHandler() {

        //接收到消息后的处理逻辑
        override fun onMessage(avimMessage: AVIMMessage, conversation: AVIMConversation, client: AVIMClient) {
            Debug.anchor(Gson().toJson(avimMessage))
            if (avimMessage is CallMessage) {
                val message = avimMessage as CallMessage
                val i = message.c_Action
                when (i) {
                    ACTION_HOLD_ON -> {
                        viewer.onUIHoldOn()
                    }
                    ACTION_HANG_UP -> {
                        viewer.onUIHungUp()
                    }
                }
            } else {
                Debug.anchor(Gson().toJson(avimMessage))
            }
        }

        override fun onMessageReceipt(message: AVIMMessage, conversation: AVIMConversation, client: AVIMClient) {
            Debug.error()
        }

    }

}

class Sender {
    val clientId = LeanCloud.getInstance().clientId
    val mClient = AVIMClient.getInstance(clientId)

    init {

    }

    /**
     * 拨打
     */
    fun call(phone: String) {
        Debug.error(phone)
        mClient.getOnlineClients(Arrays.asList(phone), object : AVIMOnlineClientsCallback() {
            override fun done(list: List<String>, e: AVIMException?) {
                if (e == null) {
                    Debug.error("在线成员：" + ToString.toString(list))
                    val message = CallMessage()
                    message.text = "正在呼叫你..."
                    message.c_Title = "主叫"
                    message.c_Action = CallMessage.ACTION_CALL
                    message.c_To = phone
                    message.c_From = clientId

                    if (Empty.isEmpty(list)) {
                        sendPushWithAction(phone, message)
                    } else {
                        getConversation(phone, object : AVIMConversationCreatedCallback() {
                            override fun done(conversation: AVIMConversation, e: AVIMException?) {
                                if (e == null) {
                                    doSend(conversation, message)
                                } else {
                                    Debug.error("getConversation():" + e)
                                    Toast.show("【主叫】出错！$e")
                                }
                            }
                        })
                    }

                } else {
                    Debug.error("getOnlineClients():" + e)
                    Toast.show("【主叫】出错！$e")
                }
            }
        })

    }

    fun holdOn(from: String) {
        getConversation(from, object : AVIMConversationCreatedCallback() {
            override fun done(conversation: AVIMConversation, e: AVIMException?) {
                if (e == null) {
                    val message = CallMessage()
                    message.text = "来电通话消息！"
                    message.c_Title = "接听"
                    message.c_Action = CallMessage.ACTION_HOLD_ON
                    message.c_To = from
                    message.c_From = clientId

                    doSend(conversation, message)
                } else {
                    Debug.error("getConversation():" + e)
                    Toast.show("【接听】出错！$e")
                }
            }
        })
    }

    fun hungUp(to: String) {
        mClient.getOnlineClients(Arrays.asList(to), object : AVIMOnlineClientsCallback() {
            override fun done(list: List<String>, e: AVIMException?) {
                if (e == null) {
                    val message = CallMessage()
                    message.text = "你有未接来电！"
                    message.c_Title = "挂断"
                    message.c_Action = CallMessage.ACTION_HANG_UP
                    message.c_To = to
                    message.c_From = clientId

                    if (Empty.isEmpty(list)) {
                        sendPushWithAction(to, message)
                    } else {
                        getConversation(to, object : AVIMConversationCreatedCallback() {
                            override fun done(conversation: AVIMConversation, e: AVIMException?) {
                                if (e == null) {
                                    doSend(conversation, message)
                                } else {
                                    Debug.error("getConversation():" + e)
                                    Toast.show("【挂断】出错！$e")
                                }
                            }
                        })
                    }

                } else {
                    Debug.error("getOnlineClients():" + e)
                    Toast.show("【主叫】出错！$e")
                }
            }
        })
    }

    fun getConversation(phone: String, callback: AVIMConversationCreatedCallback) {
        mClient.createConversation(Arrays.asList(phone), "$clientId & $phone", null, false, true, callback)
    }

    fun doSend(conversation: AVIMConversation, message: CallMessage) {
        val messageOption = AVIMMessageOption()
        messageOption.pushData = "自定义离线消息推送内容"
        conversation.sendMessage(message, messageOption, object : AVIMConversationCallback() {
            override fun done(e: AVIMException?) {
                if (e == null) {
                    Debug.error("【${message.c_Title}】成功！")
                } else {
                    Debug.error("【${message.c_Title}】失败！" + e)
                }
            }
        })
    }


    // 仅为测试的 action,如果开发者要自定义的话,注意要同时替换 Manifest 中自定义 receiver 的 action
    val CUSTOM_ACTION = "cn.leancloud.mixpush.testaction"

    /**
     * 发送带 action 的消息
     * 小米手机:弹出通知栏
     * 华为手机&普通手机:发送到用户自定义 receiver
     */
    fun sendPushWithAction(who: String, message: CallMessage) {
        val pushQuery = AVInstallation.getQuery()
        pushQuery.whereEqualTo("uid", who)
        val jsonObject = JSONObject()
        try {
            jsonObject.put("title", "【${message.c_From}】${message.text}")
            jsonObject.put("alert", "视频通话")
            jsonObject.put("action", CUSTOM_ACTION)
            jsonObject.put("c_data", Gson().toJson(message))
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        AVPush.sendDataInBackground(jsonObject, pushQuery, null)
    }


    /**
     * 发送透传消息
     * 小米手机:丢弃
     * 华为手机:丢弃
     * 普通手机:弹出通知栏
     */
    fun sendPassThroughPushWithoutAction() {
        val pushQuery = AVInstallation.getQuery()
        pushQuery.whereEqualTo("installationId", "55c43a6e7d37a807eb3e933ed1f94da4")
        val jsonObject = JSONObject()
        try {
            jsonObject.put("title", "[Tom]正在呼叫你...")
            jsonObject.put("alert", "视频通话")
            jsonObject.put("action", CUSTOM_ACTION)
            jsonObject.put("silent", true)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        AVPush.sendDataInBackground(jsonObject, pushQuery, null)
    }
}

class Ring(val context: Context) {

    private var mSoundPool: SoundPool = SoundPool(5, AudioManager.STREAM_SYSTEM, 5)
    private var mSoundID: Int = 0
    private var streamId: Int = 0


    fun load() {
        mSoundID = mSoundPool!!.load(context.applicationContext, R.raw.phonering, 1)
        mSoundPool!!.setOnLoadCompleteListener { _, _, _ -> playSound() }
    }

    fun playSound() {
        streamId = mSoundPool.play(
                mSoundID,
                1.0f, //左耳道音量【0~1】
                1.0f, //右耳道音量【0~1】
                5, //播放优先级【0表示最低优先级】
                -1, //循环模式【0表示循环一次，-1表示一直循环，其他表示数字+1表示当前数字对应的循环次数】
                1.0f          //播放速度【1是正常，范围从0~2】
        )
    }

    fun stop() {
        mSoundPool.stop(streamId)
        mSoundPool.release()
    }

}