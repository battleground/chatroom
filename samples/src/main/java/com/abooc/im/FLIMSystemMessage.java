package com.abooc.im;

import com.avos.avoscloud.im.v2.AVIMMessageCreator;
import com.avos.avoscloud.im.v2.AVIMMessageField;
import com.avos.avoscloud.im.v2.AVIMMessageType;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;

import java.util.Map;

/**
 * Created by dayu on 2017/1/10.
 */

@AVIMMessageType(
        type = 1
)
public class FLIMSystemMessage extends AVIMTypedMessage {
    @AVIMMessageField(
            name = "_text"
    )
    String text;
    @AVIMMessageField(
            name = "_action"
    )
    int action;
    @AVIMMessageField(
            name = "_attrs"
    )
    Map<String, Object> attrs;
    public static final Creator<FLIMSystemMessage> CREATOR = new AVIMMessageCreator(FLIMSystemMessage.class);

    public FLIMSystemMessage() {
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map<String, Object> getAttrs() {
        return this.attrs;
    }

    public void setAttrs(Map<String, Object> attr) {
        this.attrs = attr;
    }

    public int getAction() {
        return this.action;
    }

    public void setAction(int action) {
        this.action = action;
    }

}
