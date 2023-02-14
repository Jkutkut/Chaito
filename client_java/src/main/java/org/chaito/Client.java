package org.chaito;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private static Socket socket = null;
    private static DataInputStream input = null;
    private static DataOutputStream output = null;

    public static void main(String[] args) throws IOException {
        int port = 3232; // TODO get this from the user
        String host = "localhost"; // TODO get this from the user
        String user = "Maria"; // TODO get this from the user

        connect(user, host, port);

        send("Holiiiiiiiiiiii");
        send("Que tal?");

//        InetAddress i = socket.getInetAddress();
//
//        //Puerto local de conexión del Socket
//        System.out.println("Puerto Local: " +
//                socket.getLocalPort());
//        //Puerto de la máquina remota
//        System.out.println("Puerto Remoto: " +
//                socket.getPort());
//        System.out.println("Nombre Host/IP: " +
//                socket.getInetAddress());
//        //Host de la máquina remota
//        System.out.println("Host Remoto: " +
//                i.getHostName().toString());
//        //Diercción IP de la máquina remota a la que se conecta, en este caso el host local
//        System.out.println("IP Host Remoto: " + i.getHostAddress().toString());

        close();
    }

    private static void connect(String username, String host, int port) throws IOException {
        socket = new Socket(host, port);
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
        send(username);
    }

    private static void send(String message) throws IOException {
        output.writeUTF(message);
    }

    private static void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
        if (input != null) {
            input.close();
        }
        if (output != null) {
            output.close();
        }
    }
}
