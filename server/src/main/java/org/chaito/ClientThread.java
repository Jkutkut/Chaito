package org.chaito;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientThread extends Thread {

    private final Socket socket;
    private final ServerAPI server;
    private final String username;
    private final DataInputStream input;
    private final DataOutputStream output;

    public ClientThread(ServerAPI server, Socket ws) throws IOException {
        super();
        this.socket = ws;
        this.server = server;
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
        this.username = input.readUTF();
        // TODO be able to stop the thread from the server
    }

    @Override
    public void run() {
        String msg;
        while (true) {
            try {
                msg = input.readUTF();
                System.out.println("Message: " + msg);
                // TODO send the message to the server
            }
            catch (IOException e) {
                System.out.println("Client disconnected");
                break;
            }
        }
        close();
    }

    public void close() {
        try {
            if (input != null)
                input.close();
            if (output != null)
                output.close();
            if (socket != null)
                socket.close();
        }
        catch (IOException e) {
            System.err.println("Not able to close the client thread");
        }
    }

    // GETTER
    public String getUsername() {
        return username;
    }
}