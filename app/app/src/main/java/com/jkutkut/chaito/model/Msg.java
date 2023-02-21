package com.jkutkut.chaito.model;

import java.io.Serializable;

public class Msg implements Serializable {
    public static final long serialVersionUID = 1L;

    private static final String SERVER_TARGET = "SERVER";
    public static final String ALL_TARGET = "ALL";

    private final String target;
    private final String sender;
    private final String msg;

    public Msg(String target, String sender, String msg) {
        this.target = target;
        this.sender = sender;
        this.msg = msg;
    }

    // GETTTERS

    public String getTarget() {
        return target;
    }

    public String getSender() {
        return sender;
    }

    public String getMsg() {
        return msg;
    }
}
