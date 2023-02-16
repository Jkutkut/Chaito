package org.chaito;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements ServerAPI {
    private static final int PORT = 3232;

    public static final String SERVER_TARGET = "SERVER";
    public static final String ALL_TARGET = "ALL";

    private boolean running;
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
        System.out.println("Server started on port " + server.getLocalPort());

        while (running) { // TODO add a way to stop the server
            addClient(server.accept());
        }

        // TODO stop all the threads

        server.close();
        System.out.println("Server stopped");
    }

    public synchronized void send(String target, String msg) {
        // TODO
        // TODO remove all the terminated threads
    }

    private synchronized void addClient(Socket client) {
        try {
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
