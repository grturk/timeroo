package com.example.stillringstimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.os.CountDownTimer;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private CountDownTimer countDownTimer;
    private MediaPlayer mediaPlayerStart;
    private MediaPlayer mediaPlayerStop;
    private Button_up_down button_up_down; // logika za spreminjanje startTime
    private long startTime;
    private TextView seconds;
    private TextView hundredths;
    private TextView minutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        seconds = findViewById(R.id.seconds);
        hundredths = findViewById(R.id.hundredths);
        minutes = null;
        button_up_down = new Button_up_down(seconds, hundredths, false, minutes);

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> startTimer());

        Button seconds_up = findViewById(R.id.seconds_up_btn);
        Button seconds_down = findViewById(R.id.seconds_down_btn);
        Button hundredths_up = findViewById(R.id.hundredths_up_btn);
        Button hundredths_down = findViewById(R.id.hundredths_down_btn);

        seconds_up.setOnClickListener(v -> button_up_down.secUp());
        seconds_down.setOnClickListener(v -> button_up_down.secDown());
        hundredths_up.setOnClickListener(v -> button_up_down.hunUp());
        hundredths_down.setOnClickListener(v -> button_up_down.hunDown());

        ImageButton homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Homepage.class);
            startActivity(intent);
        });
        ImageButton backButton = findViewById(R.id.goBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (mediaPlayerStart != null) {
            mediaPlayerStart.release();
            mediaPlayerStart = null;
        }
        if (mediaPlayerStop != null) {
            mediaPlayerStop.release();
            mediaPlayerStop = null;
        }
    }

    private void startTimer() {

        if (mediaPlayerStart == null) {
            mediaPlayerStart = MediaPlayer.create(MainActivity.this, R.raw.start);
        }
        mediaPlayerStart.start();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(button_up_down.getStartTime(), 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                formatSeconds(millisUntilFinished);
            }

            @Override
            public void onFinish() {

                formatSeconds(button_up_down.getStartTime());
                // Play a sound

                if (mediaPlayerStop == null) {
                    mediaPlayerStop = MediaPlayer.create(MainActivity.this, R.raw.finish);
                }
                mediaPlayerStop.start();

            }



        }.start();
    }

    private void formatSeconds(long millisUntilFinished) {

        long seconds_i = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
        long hundredths_i = (millisUntilFinished % 1000) / 10;

        String sec = String.format("%02d", seconds_i);
        String hun = String.format("%02d", hundredths_i);

        seconds.setText(sec);
        hundredths.setText(hun);
    }

}