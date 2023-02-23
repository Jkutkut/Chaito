package com.jkutkut.chaito;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.jkutkut.chaito.custom.CustomEditText;
import com.jkutkut.chaito.exception.InvalidDataException;
import com.jkutkut.chaito.model.Msg;
import com.jkutkut.chaito.rvUtil.MsgAdapter;
import com.jkutkut.chaito.threads.ClientUI;
import com.jkutkut.chaito.threads.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ChatActivity extends AppCompatActivity implements ClientUI {
    public static final String USER_KEY = "user";
    public static final String HOST_KEY = "host";
    public static final String PORT_KEY = "port";

    public static final int BACK_CODE = 0;
    public static final int SERVER_REFUSED_CODE = 1;
    public static final int SERVER_CLOSED_CODE = 2;

    private RadioGroup msgType;
    private EditText etxtWhisperTo;

    private CustomEditText etxtMsg;
    private RecyclerView rvChat;

    private Server server;
    private ArrayList<Msg> msgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ImageButton btnBack = findViewById(R.id.ibtnBack);
        msgType = findViewById(R.id.msgType);
        etxtWhisperTo = findViewById(R.id.etxtWhisperTo);
        ImageButton btnSend = findViewById(R.id.btnSend);
        etxtMsg = findViewById(R.id.etxtMsg);
        etxtMsg.setClickFeedback(getColor(R.color.etxt_color), getColor(R.color.etxt_hightlight));
        rvChat = findViewById(R.id.rvChat);
        rvChat.setLayoutManager(new LinearLayoutManager(this));

        String user = getIntent().getStringExtra(USER_KEY);
        String host = getIntent().getStringExtra(HOST_KEY);
        int port = getIntent().getIntExtra(PORT_KEY, -1);

        msgs = new ArrayList<>();
        rvChat.setAdapter(new MsgAdapter(user, msgs));

        server = null;
        try {
            server = new Server(this, user, host, port);
        }
        catch (InvalidDataException e) {
            end(SERVER_REFUSED_CODE);
            return;
        }
        catch (SecurityException | IOException e) {
            end(SERVER_CLOSED_CODE);
            return;
        }
        server.start();

        btnBack.setOnClickListener(v -> onBackPressed());

        btnSend.setEnabled(true);
        btnSend.setOnClickListener(v -> this.handleSend());
    }

    private void handleSend() {
        String target;
        if (msgType.getCheckedRadioButtonId() == R.id.rbtnBroadcast) {
            target = Msg.ALL_TARGET;
        }
        else { // R.id.rbtnWhisper:
            target = etxtWhisperTo.getText().toString().trim();
            if (target.isEmpty()) {
                etxtWhisperTo.setError("Whisper target cannot be empty");
                return;
            }
        }

        String msg = Objects.requireNonNull(etxtMsg.getText()).toString().trim();
        if (msg.isEmpty()) {
            etxtMsg.setError("Message cannot be empty");
            return;
        }
        msg = msg.substring(0, 1).toUpperCase() + msg.substring(1);
        Msg msgObj = new Msg(target, server.getUser(), msg);
        server.send(msgObj);
        etxtMsg.setText("");
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
        finish();
    }
}