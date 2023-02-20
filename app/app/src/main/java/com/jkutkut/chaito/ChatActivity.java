package com.jkutkut.chaito;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatActivity extends AppCompatActivity {
    public static final String USER_KEY = "user";
    public static final String HOST_KEY = "host";
    public static final String PORT_KEY = "port";

    private Button btnSend;
    private EditText etxtMsg;

    private DataInputStream in;
    private DataOutputStream out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        btnSend = findViewById(R.id.btnSend);
        etxtMsg = findViewById(R.id.etxtMsg);

        String user = getIntent().getStringExtra(USER_KEY);
        String host = getIntent().getStringExtra(HOST_KEY);
        int port = getIntent().getIntExtra(PORT_KEY, -1);
        if (user == null || host == null || port == -1) {
            finish();
            return;
        }

        Thread p = new Thread(() -> {
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
        });
        p.start();

        try {
            p.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        btnSend.setEnabled(true);
        btnSend.setOnClickListener(v -> this.handleSend());

    }

    private void handleSend() {
        String msg = etxtMsg.getText().toString().trim();
        if (msg.isEmpty()) {
            return;
        }
        Thread p = new Thread(() -> {
            try {
                out.writeUTF(msg);
            } catch (IOException e) {
                e.printStackTrace();
                btnSend.setEnabled(false);
            }
        });
        p.start();
        try {
            p.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}