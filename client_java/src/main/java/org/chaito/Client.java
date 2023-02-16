package org.chaito;

import jkutkut.SuperScanner;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private static Socket socket = null;
    private static DataInputStream input = null;
    private static DataOutputStream output = null;

    private static final String DM_CMD = "dm";
    private static final String CHAT_CMD = "chat";
    private static final String EXIT_CMD = "chaito";

    private static final String OPTIONS_MENU = "Options:\n" +
            DM_CMD + ": Send a message to a user\n" +
            CHAT_CMD + ": Send a message to all users\n" +
            EXIT_CMD + ": Exit";
    private static final  String[] CMDS = {DM_CMD, CHAT_CMD, EXIT_CMD};

    public static void main(String[] args) throws IOException {
        int port = 3232; // TODO get this from the user
        String host = "localhost"; // TODO get this from the user
        String user = "Maria"; // TODO get this from the user

        connect(user, host, port);
        // TODO add listener in a new thread
        //      Idea: detect the @user and alert the user

        SuperScanner sc = new SuperScanner.En(System.in);
        String op;
        while (true) {


        }

        sc.close();
        close();
    }

    private static void connect(String username, String host, int port) throws IOException {
        socket = new Socket(host, port);
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
        send(username);
    }

    private static void send(String message) throws IOException {
        output.writeUTF(message);
    }

    private static void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
        if (input != null) {
            input.close();
        }
        if (output != null) {
            output.close();
        }
    }
}
