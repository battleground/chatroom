package com.abooc.im.message;

import com.abooc.util.Debug;
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

    public String getC_To() {
        return (String) this.getAttrs().get("c_to");
    }

    public void setC_To(String to) {
        this.getAttrs().put("c_to", to);
    }

    public String getC_From() {
        return (String) this.getAttrs().get("c_from");
    }

    public void setC_From(String from) {
        this.getAttrs().put("c_from", from);
    }

    public int getC_Action() {
        Object obj = this.getAttrs().get("c_action");
        return safeToInt(obj, -1);
    }

    public void setC_Action(int action) {
        this.getAttrs().put("c_action", String.valueOf(action));
    }

    public String getC_Title() {
        return (String) this.getAttrs().get("c_title");
    }

    public void setC_Title(String title) {
        this.getAttrs().put("c_title", title);
    }

    public String getC_Extras() {
        return (String) this.getAttrs().get("c_extras");
    }

    public void setC_Extras(String extras) {
        this.getAttrs().put("c_extras", extras);
    }

    int safeToInt(Object obj, int defValue) {
        if (obj instanceof String) {
            try {
                return Integer.valueOf((String) obj);
            } catch (NumberFormatException e) {
                Debug.error(e);
            }
        }
        return defValue;
    }


}
