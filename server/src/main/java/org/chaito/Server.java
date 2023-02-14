package org.chaito;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 3232;
    private static boolean running = true;

    public static void main(String[] args) {
        ServerSocket server;

        try {
            server = new ServerSocket(PORT);
        } catch (IOException e) {
            System.err.println("Not able to create the server");
            return;
        }
        System.out.println("Server started on port " + server.getLocalPort());
        Socket client;
        DataInputStream input;
        DataOutputStream output;
        while (running) { // TODO add a way to stop the server
            try {
                client = server.accept();
                System.out.println("Client connected: " + client.getInetAddress());

                input = new DataInputStream(client.getInputStream());
                output = new DataOutputStream(client.getOutputStream());

                String username = input.readUTF();
                System.out.println("Username: " + username);
                // TODO

                // Get msgs until client closes the connection
                while (true) {
                    try {
                        String message = input.readUTF();
                        System.out.println("Message: " + message);
                    }
                    catch (IOException e) {
                        System.out.println("Client disconnected");
                        break;
                    }
                }
                client.close();

                break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Server stopped");
    }
}
