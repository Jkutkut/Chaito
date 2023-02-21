package com.jkutkut.chaito;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(this, ChatActivity.class);
        i.putExtra(ChatActivity.USER_KEY, "Manolo");
        i.putExtra(ChatActivity.HOST_KEY, "10.34.177.197");
        i.putExtra(ChatActivity.PORT_KEY, 3232);
        // TODO get data from the UI
        startActivity(i);
    }
}