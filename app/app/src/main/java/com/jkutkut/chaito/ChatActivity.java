package com.jkutkut.chaito;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;

import com.jkutkut.chaito.model.Msg;
import com.jkutkut.chaito.rvUtil.MsgAdapter;
import com.jkutkut.chaito.threads.ClientUI;
import com.jkutkut.chaito.threads.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements ClientUI {
    public static final String USER_KEY = "user";
    public static final String HOST_KEY = "host";
    public static final String PORT_KEY = "port";

    private Button btnSend;
    private EditText etxtMsg;
    private RecyclerView rvChat;

    private Server server;
    private ArrayList<Msg> msgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btnSend = findViewById(R.id.btnSend);
        etxtMsg = findViewById(R.id.etxtMsg);
        rvChat = findViewById(R.id.rvChat);
        rvChat.setLayoutManager(new LinearLayoutManager(this));

        String user = getIntent().getStringExtra(USER_KEY);
        String host = getIntent().getStringExtra(HOST_KEY);
        int port = getIntent().getIntExtra(PORT_KEY, -1);
        if (user == null || host == null || port == -1) {
            finish();
            return;
        }

        msgs = new ArrayList<>();
        rvChat.setAdapter(new MsgAdapter(msgs));

        server = new Server(this, user, host, port);
        server.start();

        btnSend.setEnabled(true);
        btnSend.setOnClickListener(v -> this.handleSend());
    }

    private void handleSend() {
        String msg = etxtMsg.getText().toString().trim();
        if (msg.isEmpty()) {
            return;
        }
        server.send(msg);
    }

    public void handleReceive(Msg msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                msgs.add(msg);
                rvChat.getAdapter().notifyDataSetChanged();
            }
        });
    }
}