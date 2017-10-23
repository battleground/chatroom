package com.facetime.mvp

import com.abooc.im.LeanCloud
import com.abooc.im.Saver
import com.abooc.im.activity.LoginActivity
import com.abooc.im.message.CallMessage
import com.abooc.util.Debug
import com.abooc.widget.Toast
import com.avos.avoscloud.AVMixpushManager
import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback
import com.avos.avoscloud.im.v2.callback.AVIMOnlineClientsCallback
import com.facetime.CallOutActivity
import com.facetime.FaceTimeActivity
import org.lee.java.util.Empty
import org.lee.java.util.ToString
import java.util.*

/**
 * Created by dayu on 2017/10/16.
 */

interface CallViewer {

}


class CallOutPresenter(val viewer: CallViewer) {

    val mSender = Sender()

    init {
        val callOutViewer = viewer as CallOutActivity
        callOutViewer.onCallEvent = {
            Debug.error()
            FaceTimeActivity.show(viewer.applicationContext, callOutViewer.getPhone(), CallMessage.ACTION_HANG_UP)
            mSender.call(callOutViewer.getPhone())
        }
        callOutViewer.onPushCallback = {
            val clientId = LeanCloud.getInstance().clientId

            val message = CallMessage()
            message.text = "正在呼叫你..."
            message.c_Title = "主叫"
            message.c_Action = CallMessage.ACTION_CALL
            message.c_To = callOutViewer.getPhone()
            message.c_From = clientId

            mSender.sendPushWithAction(callOutViewer.getPhone(), message)
        }
        callOutViewer.onPushNotificationHungUp = {
            val clientId = LeanCloud.getInstance().clientId

            val message = CallMessage()
            message.text = "你有未接来电！"
            message.c_Title = "挂断"
            message.c_Action = CallMessage.ACTION_HANG_UP
            message.c_To = callOutViewer.getPhone()
            message.c_From = clientId

            mSender.sendPushWithAction(callOutViewer.getPhone(), message)
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



