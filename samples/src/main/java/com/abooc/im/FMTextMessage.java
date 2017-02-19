package com.abooc.im;

import com.avos.avoscloud.im.v2.AVIMMessageType;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import java.util.HashMap;

/**
 * Created by dayu on 2017/1/15.
 */

@AVIMMessageType(
        type = -1
)
public class FMTextMessage extends AVIMTextMessage {

    public FMTextMessage() {
        setAttrs(new HashMap<String, Object>());
    }

    public String getUsername() {
        return (String) this.getAttrs().get("username");
    }

    public void setUsername(String username) {
        this.getAttrs().put("username", username);
    }

    public String getAvatar() {
        return (String) this.getAttrs().get("avatar");
    }

    public void setAvatar(String avatar) {
        this.getAttrs().put("avatar", avatar);
    }

    public String getUid() {
        return (String) this.getAttrs().get("uid");
    }

    public void setUid(String uid) {
        this.getAttrs().put("uid", uid);
    }
}
