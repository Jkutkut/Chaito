package com.jkutkut.chaito.threads;

import com.jkutkut.chaito.model.Msg;

public interface ClientUI {
    void handleReceive(Msg msg);
    void handleDisconnect();
}
