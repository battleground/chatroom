package com.abooc.im;

import com.google.gson.JsonObject;

public class NotifyMessage {
    public String alert;
    public String title;
    public boolean silent;
    public Payload payload;

    class Payload {
        public String type;
        public JsonObject model;
    }
}
