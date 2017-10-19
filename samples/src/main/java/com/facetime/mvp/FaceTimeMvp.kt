package com.facetime.mvp


import com.abooc.im.message.CallMessage
import com.abooc.im.message.CallMessage.ACTION_HANG_UP
import com.abooc.im.message.CallMessage.ACTION_HOLD_ON
import com.abooc.util.Debug
import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMMessage
import com.avos.avoscloud.im.v2.AVIMMessageHandler
import com.facetime.FaceTime
import com.google.gson.Gson

/**
 * Created by dayu on 2017/10/18.
 */

interface FaceTimeViewer {

    fun onUIHoldOn()
    fun onUIHungUp()

    fun onTick(seconds: Long, time: String)

}


class FaceTimePresenter(val viewer: FaceTimeViewer) {

    var uid: String? = null
    val mSender = Sender()
    var iReceiver: Receiver? = null

    var isConnected = false

    init {
    }

    fun onCreate() {
        iReceiver = Receiver()
        iReceiver?.onCreate(viewer)

        val faceTimeViewer = viewer as FaceTime
        faceTimeViewer.onHoldOnEvent = {
            Debug.error(uid)
            mSender.holdOn(uid!!)
        }
        faceTimeViewer.onHungUpEvent = {
            Debug.error(uid)
            mSender.hungUp(uid!!)
        }
    }

    fun destroy() {
        iReceiver?.destroy()
        isConnected = false
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