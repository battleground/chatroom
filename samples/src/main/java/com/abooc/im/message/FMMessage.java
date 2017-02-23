package com.abooc.im.message;

import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import java.util.HashMap;

/**
 * Created by dayu on 2017/1/15.
 */

public class FMMessage extends AVIMTextMessage {

    public FMMessage() {
        setAttrs(new HashMap<String, Object>());
    }

    public String getType() {
        return (String) this.getAttrs().get("type");
    }

    public void setType(String type) {
        this.getAttrs().put("type", type);
    }

    public String getAction() {
        return (String) this.getAttrs().get("action");
    }

    public void setAction(String action) {
        this.getAttrs().put("action", action);
    }
}
