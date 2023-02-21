package com.jkutkut.chaito;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
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

@RequiresApi(api = Build.VERSION_CODES.O)
public class ChatActivity extends AppCompatActivity implements ClientUI {
    public static final String USER_KEY = "user";
    public static final String HOST_KEY = "host";
    public static final String PORT_KEY = "port";

    public static final int BACK_CODE = 0;
    public static final int SERVER_REFUSED_CODE = 1;
    public static final int SERVER_CLOSED_CODE = 2;

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

        msgs = new ArrayList<>();
        rvChat.setAdapter(new MsgAdapter(msgs));

        server = null;
        try {
            server = new Server(this, user, host, port);
        }
        catch (SecurityException | IOException e) {
            end(SERVER_REFUSED_CODE);
            return;
        }
        server.start();

        btnSend.setEnabled(true);
        btnSend.setOnClickListener(v -> this.handleSend());
    }

    private void handleSend() {
        String msg = etxtMsg.getText().toString().trim();
        if (msg.isEmpty()) {
            return;
        }
        // TODO change target
        Msg msgObj = new Msg(Msg.ALL_TARGET, server.getUser(), msg);
        server.send(msgObj);
    }

    public void handleReceive(Msg msg) {
        runOnUiThread(() -> {
            msgs.add(msg);
            rvChat.getAdapter().notifyDataSetChanged();
        });
    }

    public void handleDisconnect() {
        if (server == null) return;
        end(SERVER_CLOSED_CODE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        end(BACK_CODE);
    }

    private void end(int resultCode) {
        System.out.println("Ending with code " + resultCode);
        if (server != null) {
            synchronized (server) {
                if (server == null) return;
                server.close();
                server = null;
            }
        }
        setResult(resultCode);
        finish(); // TODO should be done with back?
    }
}