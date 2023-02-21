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
    private boolean running;

    private final ClientUI ui;
    private final String user;
    private final String host;
    private final int port;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public Server(ClientUI ui, String user, String host, int port) throws SecurityException, IOException {
        this.ui = ui;
        this.user = user;
        this.host = host;
        this.port = port;
        this.running = true;

        System.out.println("********* Connecting to " + this.host + ":" + this.port + " *********");
        socket = new Socket(host, port);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        System.out.println("********* Connected to " + host + ":" + port + " *********");
        out.writeUTF(user);
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
                break;
            }
        }
        this.close();
        System.out.println("Connection closed");
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

    public synchronized void close() {
        ui.handleDisconnect();
        running = false;
        try {
            if (in != null) {
                in.close();
                in = null;
            }
            if (out != null) {
                out.close();
                out = null;
            }
            if (socket != null) {
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
