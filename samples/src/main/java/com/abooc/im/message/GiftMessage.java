package com.abooc.im.message;

import com.avos.avoscloud.im.v2.AVIMMessageType;

/**
 * Created by dayu on 2017/2/23.
 */

@AVIMMessageType(
        type = -1
)
public class GiftMessage extends FMTextMessage {

    public GiftMessage(){
        super();
        setType("103");
    }

    public String getName() {
        return (String) this.getAttrs().get("gift_name");
    }

    public void setName(String giftName) {
        this.getAttrs().put("gift_name", giftName);
    }

    public String getCode() {
        return (String) this.getAttrs().get("gift_code");
    }

    public void setCode(String giftID) {
        this.getAttrs().put("gift_code", giftID);
    }

}
