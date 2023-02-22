package com.jkutkut.chaito;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jkutkut.chaito.custom.CustomActivity;
import com.jkutkut.chaito.custom.CustomButton;
import com.jkutkut.chaito.custom.CustomEditText;

public class MainActivity extends CustomActivity {

    private CustomEditText etxtUsername;
    private CustomEditText etxtHost;
    private CustomEditText etxtPort;

    private static final String VALID_IP = "^(?:\\d{1,3}\\.){3}\\d{1,3}$";

    private ActivityResultLauncher<Intent> chatActivityResultLauncher;

    @SuppressLint("MissingSuperCall")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);

        etxtUsername = findViewById(R.id.etxtUsername);
        etxtHost = findViewById(R.id.etxtHost);
        etxtPort = findViewById(R.id.etxtPort);
        CustomButton btnLogin = findViewById(R.id.btnLogin);

        etxtUsername.setText("Jkutkut");
        etxtUsername.setClickFeedback(getColor(R.color.teal_700), getColor(R.color.teal_200)); // TODO Change this color
        etxtHost.setText("10.34.177.197");
        etxtHost.setClickFeedback(getColor(R.color.teal_700), getColor(R.color.teal_200)); // TODO Change this color
        etxtPort.setText("3232");
        etxtPort.setClickFeedback(getColor(R.color.teal_700), getColor(R.color.teal_200)); // TODO Change this color

        btnLogin.setOnClickListener(v -> login());
        btnLogin.setClickFeedback(getColor(R.color.teal_700)); // TODO Change this color

        chatActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                System.out.println("*********************** Result code: " + result.getResultCode());
                switch (result.getResultCode()) {
                    case ChatActivity.BACK_CODE:
                        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
                        break;
                    case ChatActivity.SERVER_REFUSED_CODE:
                        etxtUsername.setError("This user is already logged in!");
                        break;
                    case ChatActivity.SERVER_CLOSED_CODE:
                        Toast.makeText(this, "Server is not connecting!", Toast.LENGTH_SHORT).show();
                        etxtHost.setError("Is the server running?");
                        break;
                }
            }
        );
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
        chatActivityResultLauncher.launch(i);
    }
}