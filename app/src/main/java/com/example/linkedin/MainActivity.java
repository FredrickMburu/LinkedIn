package com.example.linkedin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Handler handler = new Handler();
    TimerTask task;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a new TimerTask object.
        task = new TimerTask() {
            @Override
            public void run() {
                // Start the LoginPage activity.
                Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(intent);
                finish();

                // Cancel the timer task.
                handler.removeCallbacks(task);
            }
        };

        // Schedule the TimerTask object to be executed after 2 seconds.
        new Timer().schedule(task, 2000);
    }
}
