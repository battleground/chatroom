package com.facetime.mvp

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import com.abooc.im.LeanCloud
import com.abooc.im.R
import com.abooc.im.Saver
import com.abooc.im.activity.LoginActivity
import com.abooc.im.message.GiftMessage
import com.abooc.util.Debug
import com.abooc.widget.Toast
import com.avos.avoscloud.AVInstallation
import com.avos.avoscloud.AVMixpushManager
import com.avos.avoscloud.AVPush
import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.AVIMMessageOption
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback
import com.avos.avoscloud.im.v2.callback.AVIMOnlineClientsCallback
import com.facetime.CallOut
import org.json.JSONException
import org.json.JSONObject
import org.lee.java.util.Empty
import org.lee.java.util.ToString
import java.util.*

/**
 * Created by dayu on 2017/10/16.
 */

interface Viewer {

}


class CallOutPresenter(val viewer: Viewer) {

    val mSender = Sender()

    init {
        val callOutViewer = viewer as CallOut
        callOutViewer.onCallEvent = {
            Debug.error()
            mSender.call(callOutViewer.getPhone())
        }
        callOutViewer.onPushCallback = {
            mSender.sendPushWithAction(callOutViewer.getPhone())
        }
        callOutViewer.onExitEvent = {
            val clientId = LeanCloud.getInstance().clientId
            val mClient = AVIMClient.getInstance(clientId)
            mClient.close(object : AVIMClientCallback() {
                override fun done(avimClient: AVIMClient, e: AVIMException?) {
                    if (e == null) {
                        Debug.error("已退出账号：$clientId")
                        Saver.remove(viewer.baseContext)
                        AVMixpushManager.unRegisterMixPush()
                        callOutViewer.finish()
                        LoginActivity.launch(viewer.baseContext)
                    } else {
                        Debug.error("退出账号：$clientId，错误：$e")
                        callOutViewer.error(e.toString())
                        Toast.show(e.message)
                    }
                }

            })
        }
        callOutViewer.onQueryEvent = {
            Debug.error(callOutViewer.getPhone())

            mSender.mClient.getOnlineClients(Arrays.asList(callOutViewer.getPhone()), object : AVIMOnlineClientsCallback() {
                override fun done(list: List<String>, e: AVIMException?) {
                    if (e == null) {
                        Debug.error("在线成员：" + ToString.toString(list))
                        if (Empty.isEmpty(list)) {
                            callOutViewer.offline()
                        } else {
                            callOutViewer.online()
                        }
                    } else {
                        Debug.error("getOnlineClients():" + e)
                        callOutViewer.error(e.toString())
                    }
                }
            })
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
                    if (Empty.isEmpty(list)) {
                        sendPushWithAction(phone)
                    } else {
                        getConversation(phone, object : AVIMConversationCreatedCallback() {
                            override fun done(conversation: AVIMConversation, e: AVIMException?) {
                                if (e == null) {
                                    val giftMessage = GiftMessage()
                                    giftMessage.text = "发送一个礼物！"
                                    giftMessage.name = "【普通礼物】"
                                    giftMessage.code = "GF-01-333"
                                    giftMessage.money = 100
                                    giftMessage.uid = clientId

                                    doSend(conversation, giftMessage)
                                } else {

                                }
                            }
                        })
                    }

                } else {
                    Debug.error("getOnlineClients():" + e)
                }
            }
        })

    }

    fun getConversation(phone: String, callback: AVIMConversationCreatedCallback) {
        mClient.createConversation(Arrays.asList(phone), "$clientId & $phone", null, false, true, callback)
    }

    internal fun doSend(conversation: AVIMConversation, message: GiftMessage) {
        val messageOption = AVIMMessageOption()
        messageOption.pushData = "自定义离线消息推送内容"
        conversation.sendMessage(message, messageOption, object : AVIMConversationCallback() {
            override fun done(e: AVIMException?) {
                if (e == null) {
                    Toast.show("已发送！")
                } else {
                    Debug.error("发送失败！" + e)
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
    fun sendPushWithAction(who: String) {
        val pushQuery = AVInstallation.getQuery()
        pushQuery.whereEqualTo("uid", who)
        //        pushQuery.whereEqualTo("installationId", "e691ccb5e4330dc93b22f72a3af0b158");
        val jsonObject = JSONObject()
        try {
            jsonObject.put("title", "[Tom]正在呼叫你...")
            jsonObject.put("alert", "视频通话")
            jsonObject.put("action", CUSTOM_ACTION)
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

    private var mSoundPool: SoundPool = SoundPool(5, AudioManager.STREAM_RING, 5)
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


