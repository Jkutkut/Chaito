package com.jkutkut.chaito.threads;

import com.jkutkut.chaito.model.Msg;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server extends Thread {
    private boolean running;

    private ClientUI ui;
    private String user;
    private String host;
    private int port;

    private DataInputStream in;
    private DataOutputStream out;

    public Server(ClientUI ui, String user, String host, int port) {
        this.ui = ui;
        this.user = user;
        this.host = host;
        this.port = port;
        this.running = true;

        try {
            System.out.println("********* Connecting to " + host + ":" + port + " *********");
            Socket socket = new Socket(host, port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            System.out.println("********* Connected to " + host + ":" + port + " *********");
            out.writeUTF(user);

        } catch (UnknownHostException | SecurityException e) {
            e.printStackTrace();
            System.out.println("********* Failed to connect to " + host + ":" + port + " *********");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("********* Failed to create streams *********");
        }
        // TODO handle errors
    }

    public void run() {
        while (running) {
            try {
                String msg = in.readUTF();
                this.ui.handleReceive(new Msg(Msg.ALL_TARGET, "??", msg));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
