package com.jkutkut.chaito.custom;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jkutkut.chaito.R;

/**
 * My own version of an Android Activity.
 *
 * @author jkutkut
 */
public class CustomActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState, final int layout) {
        super.onCreate(savedInstanceState);
        setContentView(layout);

        // All custom activities have a floating button to change the theme
        FloatingActionButton fabMode = findViewById(R.id.fabMode);
        fabMode.setOnClickListener(this::toggleDarkLightMode);
    }

    /**
     * Standard method to send a message to the user.
     * @param msg Message to show.
     */
    protected void alert(String msg) {
        Toast.makeText(
                this,
                msg,
                Toast.LENGTH_LONG
        ).show();
    }

    /**
     * Toggle between dark and light mode.
     * @param ignoredView View send by the click listener (not used).
     */
    protected void toggleDarkLightMode(View ignoredView) {
        if (darkMode())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    /**
     * Check if the current mode is dark.
     * @return True if the current mode is dark.
     */
    protected boolean darkMode() {
        int nightModeFlags = this.getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
}