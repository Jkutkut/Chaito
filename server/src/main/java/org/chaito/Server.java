package org.chaito;

import org.chaito.db.ChaitoDB;
import org.chaito.model.Msg;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server implements ServerAPI {
    private final ArrayList<ClientThread> clients;
    private final ChaitoDB db;
    private final int port;

    public Server(int port) {
        this.port = port;
        db = new ChaitoDB();
        clients = new ArrayList<>();
    }

    public void run() throws IOException {
        ServerSocket server;
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Not able to create the server");
            e.printStackTrace();
            return;
        }
        System.out.println("Server started on " + server.getLocalSocketAddress());
        System.out.println("Ctrl+C to stop the server");

        while (true) {
            addClient(server.accept());
        }
    }

    public synchronized void send(Msg msgObj) {
        String target = msgObj.target();
        String sender = msgObj.sender();
        String msg = msgObj.msg();

        db.insert(msgObj);

        System.out.println("Sending message to " + target + " from " + sender + ": " + msg);
        if (target.equals(Msg.ALL_TARGET)) {
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
            ClientThread c;
            for (int i = 0, j = 0; i < clients.size() && j < 2; i++) {
                c = clients.get(i);
                if (!c.isAlive()) { // If thread has ended, remove it
                    clients.remove(i--);
                    continue;
                }
                if (c.getUsername().equals(target) || c.getUsername().equals(sender)) {
                    c.send(msgObj);
                    j++;
                }
            }
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
        if (args.length != 1) {
            System.err.println("Specify the port!");
            return;
        }
        int port;
        try {
            port = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException e) {
            System.err.println("Port must be an integer!");
            return;
        }
        new Server(port).run();
    }
}
