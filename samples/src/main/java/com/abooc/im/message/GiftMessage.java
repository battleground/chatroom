package com.abooc.im.message;

import com.avos.avoscloud.im.v2.AVIMMessageType;

/**
 * Created by dayu on 2017/2/23.
 */

@AVIMMessageType(
        type = -1
)
public class GiftMessage extends FMTextMessage {

    public GiftMessage() {
        super();
        setType("103");
    }

    public String getGiftId() {
        return (String) this.getAttrs().get("gift_id");
    }

    public void setGiftId(String giftId) {
        this.getAttrs().put("gift_id", giftId);
    }

    public int getGiftIndex() {
        Object obj = this.getAttrs().get("gift_index");
        if (obj instanceof Integer) {
            return (int) obj;
        }
        try {
            String index = (String) this.getAttrs().get("gift_index");
            Integer integer = Integer.valueOf(index);
            return integer;
        } catch (Exception e) {

        }
        return 1;
    }

    public void setGiftIndex(int giftIndex) {
        this.getAttrs().put("gift_index", giftIndex);
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
