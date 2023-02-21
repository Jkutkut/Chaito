package org.chaito;

import org.chaito.db.ChaitoDB;
import org.chaito.model.Msg;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements ServerAPI {
    private static final int PORT = 3232;

    private static final String SERVER_TARGET = "SERVER";
    public static final String ALL_TARGET = "ALL";

    private final ArrayList<ClientThread> clients;
    private final ChaitoDB db;

    public Server() {
        db = new ChaitoDB();
        clients = new ArrayList<>();
    }

    public void run() throws IOException {
        ServerSocket server;
        try {
            server = new ServerSocket(PORT);
        } catch (IOException e) {
            System.err.println("Not able to create the server");
            return;
        }
        System.out.println("Server started on " + server.getLocalSocketAddress());

        while (true) {
            addClient(server.accept());
        }
    }

    public synchronized void send(Msg msgObj) {
        String target = msgObj.getTarget();
        String sender = msgObj.getSender();
        String msg = msgObj.getMsg();

        db.insert(msgObj);

        System.out.println("Sending message to " + target + " from " + sender + ": " + msg);
        if (target.equals(ALL_TARGET)) {
            ClientThread c;
            for (int i = 0; i < clients.size(); i++) {
                c = clients.get(i);
                if (!c.isAlive()) { // If thread has ended, remove it
                    clients.remove(i--);
                    continue;
                }
                clients.get(i).send(msgObj);
            }
        }
        else {
            System.out.println("Not implemented yet");
        }
    }

    private synchronized void addClient(Socket client) {
        try {
            System.out.println("Connecting client...");
            ClientThread clientThread = new ClientThread(this, client);

            // Check if username is already taken
            ClientThread c;
            for (int i = 0; i < clients.size(); i++) {
                c = clients.get(i);
                if (!c.isAlive()) { // If thread has ended, remove it
                    clients.remove(i--);
                    continue;
                }
                if (c.getUsername().equals(clientThread.getUsername())) {
                    System.out.println("Username already taken");
                    clientThread.sendValidConnection(false);
                    clientThread.close();
                    return;
                }
            }
            clientThread.sendValidConnection(true);
            clients.add(clientThread);
            clientThread.start();
            System.out.println("Client connected: " + clientThread.getUsername());
            for (Msg msg : db.getMsgs(clientThread.getUsername())) {
                clientThread.send(msg);
            }

        }
        catch (IOException e) {
            System.err.println("Not able to create the client thread");
        }
    }

    public static void main(String[] args) throws IOException {
        new Server().run();
    }
}
