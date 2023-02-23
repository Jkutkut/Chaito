package org.chaito;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import org.chaito.model.Msg;

public class ClientThread extends Thread implements ServerAPI {

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
    }

    @Override
    public void run() {
        String data;
        Msg msg;
        while (true) {
            try {
                data = input.readUTF();
                msg = Msg.decode(data);
                server.send(msg);
            }
            catch (IOException e) {
                System.out.println("Client disconnected");
                break;
            }
        }
        close();
    }

    public synchronized void send(Msg msg) {
        try {
            this.output.writeUTF(msg.encode());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void sendValidConnection(boolean valid) {
        final String ERROR_USER_LOGGED = "ALREADY LOGGED";
        final String OK = "OK";
        try {
            if (valid)
                this.output.writeUTF(OK);
            else
                this.output.writeUTF(ERROR_USER_LOGGED);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
