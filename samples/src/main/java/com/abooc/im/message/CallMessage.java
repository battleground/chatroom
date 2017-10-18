package com.abooc.im.message;

import com.avos.avoscloud.im.v2.AVIMMessageType;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import java.util.HashMap;

/**
 * Created by dayu on 2017/2/23.
 * <p>
 * title:String     通话消息
 * to:String        接听方
 * from:String      主叫方
 * action:Int       事件：主叫、(主动挂断、被拒)、接听
 */

@AVIMMessageType(
        type = -1
)
public class CallMessage extends AVIMTextMessage {

    public static final int ACTION_CALL = (1);
    public static final int ACTION_HOLD_ON = (2);
    public static final int ACTION_HANG_UP = (3);
//    public static final int ACTION_BLOCK = (4);

    public CallMessage() {
        setAttrs(new HashMap<String, Object>());
    }

    public String getTo() {
        return (String) this.getAttrs().get("c_to");
    }

    public void setTo(String to) {
        this.getAttrs().put("c_to", to);
    }

    public String getFrom() {
        return (String) this.getAttrs().get("c_from");
    }

    public void setFrom(String from) {
        this.getAttrs().put("c_from", from);
    }

    public int getAction() {
        Object obj = this.getAttrs().get("c_action");
        return safeToInt(obj, 0);
    }

    public void setAction(int action) {
        this.getAttrs().put("c_action", action);
    }

    public String getTitle() {
        return (String) this.getAttrs().get("c_title");
    }

    public void setTitle(String title) {
        this.getAttrs().put("c_title", title);
    }

    int safeToInt(Object obj, int defValue) {
        if (obj instanceof Integer) {
            return (int) obj;
        }
        return defValue;
    }

}
