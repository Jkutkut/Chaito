package com.jkutkut.chaito;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText etxtUsername;
    private EditText etxtHost;
    private EditText etxtPort;
    private Button btnLogin;

    private static final String VALID_IP = "^(?:\\d{1,3}\\.){3}\\d{1,3}$";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etxtUsername = findViewById(R.id.etxtUsername);
        etxtHost = findViewById(R.id.etxtHost);
        etxtPort = findViewById(R.id.etxtPort);
        btnLogin = findViewById(R.id.btnLogin);

        etxtUsername.setText("Manolo");
        etxtHost.setText("10.34.177.197");
        etxtPort.setText("3232");

        btnLogin.setOnClickListener(v -> login());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void login() {
        String user = etxtUsername.getText().toString().trim();
        String host = etxtHost.getText().toString().trim();
        String port = etxtPort.getText().toString().trim();

        if (user.length() == 0) {
            etxtUsername.setError("The user can not be empty!");
            return;
        }
        if (host.length() == 0) {
            etxtHost.setError("The host can not be empty!");
            return;
        }
        if (!host.matches(VALID_IP)) {
            etxtHost.setError("The host is not a valid IP!");
            return;
        }
        if (port.length() == 0) {
            etxtPort.setError("The port can not be empty!");
            return;
        }
        if (Integer.parseInt(port) < 0 || Integer.parseInt(port) > 65535) {
            etxtPort.setError("The port is not valid!");
            return;
        }

        Intent i = new Intent(this, ChatActivity.class);
        i.putExtra(ChatActivity.USER_KEY, user);
        i.putExtra(ChatActivity.HOST_KEY, host);
        i.putExtra(ChatActivity.PORT_KEY, Integer.parseInt(port));
        startActivity(i);
    }
}