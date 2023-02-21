package com.jkutkut.chaito.threads;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.jkutkut.chaito.model.Msg;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server extends Thread {
    private final boolean running;

    private final ClientUI ui;
    private final String user;
    private final String host;
    private final int port;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void run() {
        String data;
        Msg msg;
        while (running) {
            try {
                data = in.readUTF();
                msg = Msg.decode(data);
                this.ui.handleReceive(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void send(Msg msg) {
        try {
            out.writeUTF(msg.encode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // GETTERS
    public String getUser() {
        return user;
    }
}
