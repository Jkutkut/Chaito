package org.chaito.model;

import java.util.Base64;

public class Msg {
    private static final String COMMUNICATION_SEPARATOR = ",";

    public static final String ALL_TARGET = "ALL";

    private final String target;
    private final String sender;
    private final String msg;

    public Msg(String target, String sender, String msg) {
        this.target = target;
        this.sender = sender;
        this.msg = msg;
    }

    public String encode() {
        Base64.Encoder encoder = Base64.getEncoder();
        String targetBase64 = encoder.encodeToString(target.getBytes());
        String senderBase64 = encoder.encodeToString(sender.getBytes());
        String msgBase64 = encoder.encodeToString(msg.getBytes());

        return String.join(COMMUNICATION_SEPARATOR, targetBase64, senderBase64, msgBase64);
    }

    public static Msg decode(String encoded) {
        Base64.Decoder decoder = Base64.getDecoder();
        String[] parts = encoded.split(COMMUNICATION_SEPARATOR);
        String targetBase64 = parts[0];
        String senderBase64 = parts[1];
        String msgBase64 = parts[2];

        String target = new String(decoder.decode(targetBase64));
        String sender = new String(decoder.decode(senderBase64));
        String msg = new String(decoder.decode(msgBase64));

        return new Msg(target, sender, msg);
    }

    // GETTERS

    public String getTarget() {
        return target;
    }

    public String getSender() {
        return sender;
    }

    public String getMsg() {
        return msg;
    }

    public static void main(String[] args) {
        Msg msg = new Msg("Luis", "Manolo", "Hello World!");
        String encoded = msg.encode();
        System.out.println(encoded);
        Msg decoded = decode(encoded);
        System.out.println(decoded.getTarget() + " " + decoded.getSender() + " " + decoded.getMsg());
    }
}
