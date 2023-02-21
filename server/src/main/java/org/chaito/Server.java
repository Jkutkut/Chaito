package org.chaito;

import org.chaito.model.Msg;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements ServerAPI {
    private static final int PORT = 3232;

    private static final String SERVER_TARGET = "SERVER";
    public static final String ALL_TARGET = "ALL";

    private boolean running; // TODO needed?
    private final ArrayList<ClientThread> clients;

    public Server() {
        running = true;
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

        while (running) { // TODO add a way to stop the server
            addClient(server.accept());
        }

        // TODO stop all the threads

        server.close();
        System.out.println("Server stopped");
    }

    public synchronized void send(Msg msgObj) {
        String target = msgObj.getTarget();
        String sender = msgObj.getSender();
        String msg = msgObj.getMsg();

        System.out.println("Sending message to " + target + " from " + sender + ": " + msg);
        if (target.equals(ALL_TARGET)) {
            ClientThread c;
            for (int i = 0; i < clients.size(); i++) {
                c = clients.get(i);
                if (!c.isAlive()) { // If thread has ended, remove it
                    clients.remove(i--);
                    // TODO this may need to be removed if we want chat history
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
            clients.add(clientThread);
            clientThread.start();
            System.out.println("Client connected: " + clientThread.getUsername());
        }
        catch (IOException e) {
            System.err.println("Not able to create the client thread");
        }
    }

    public static void main(String[] args) throws IOException {
        new Server().run();
    }
}
