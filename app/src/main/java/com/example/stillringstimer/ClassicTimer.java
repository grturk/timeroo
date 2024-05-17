package com.example.stillringstimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class ClassicTimer extends AppCompatActivity {
    private CountDownTimer countDownTimer;
    private MediaPlayer mediaPlayerStart;
    private MediaPlayer mediaPlayerStop;
    private Button_up_down button_up_down; // logika za spreminjanje startTime
    private long startTime;
    private TextView minutes;
    private TextView seconds;
    private TextView hundredths;
    private Button resetButton;
    private Button startButton;
    private boolean isRunning = false;
    private long remainingTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_timer);

        minutes = findViewById(R.id.minutes);
        seconds = findViewById(R.id.seconds);
        hundredths = findViewById(R.id.hundredths);
        button_up_down = new Button_up_down(seconds, hundredths, true, minutes);

        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> startTimer());

        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(v -> resetTimer());

        Button minutes_up = findViewById(R.id.minutes_up_btn);
        Button minutes_down = findViewById(R.id.minutes_down_btn);
        Button seconds_up = findViewById(R.id.seconds_up_btn);
        Button seconds_down = findViewById(R.id.seconds_down_btn);
        Button hundredths_up = findViewById(R.id.hundredths_up_btn);
        Button hundredths_down = findViewById(R.id.hundredths_down_btn);


        minutes_up.setOnClickListener(v -> button_up_down.minUp());
        minutes_down.setOnClickListener(v -> button_up_down.minDown());
        seconds_up.setOnClickListener(v -> button_up_down.secUp());
        seconds_down.setOnClickListener(v -> button_up_down.secDown());
        hundredths_up.setOnClickListener(v -> button_up_down.hunUp());
        hundredths_down.setOnClickListener(v -> button_up_down.hunDown());

        ImageButton homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(ClassicTimer.this, Homepage.class);
            startActivity(intent);
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

    private void resetTimer() {
        formatSeconds(button_up_down.getStartTime());
    }

    private void startTimer() {

        if (!isRunning) {
            // Timer is currently stopped, start it
            if (mediaPlayerStart == null) {
                mediaPlayerStart = MediaPlayer.create(ClassicTimer.this, R.raw.start); // Add a sound file to res/raw/start.mp3
            }
            mediaPlayerStart.start();

            if (countDownTimer != null) {
                countDownTimer.cancel();
            }

            startButton.setText("PAUSE");
            isRunning = true;

            long startTime = (remainingTime > 0) ? remainingTime : button_up_down.getStartTime();
            countDownTimer = new CountDownTimer(startTime, 10) {
                @Override
                public void onTick(long millisUntilFinished) {
                    formatSeconds(millisUntilFinished);
                    remainingTime = millisUntilFinished;
                }

                @Override
                public void onFinish() {

                    if (mediaPlayerStop == null) {
                        mediaPlayerStop = MediaPlayer.create(ClassicTimer.this, R.raw.finish); // Add a sound file to res/raw/finish.mp3
                    }
                    mediaPlayerStop.start();
                    startButton.setText("START");
                    isRunning = false;
                    remainingTime = 0;
                    formatSeconds(0);
                }
            }.start();
        } else {
            // Timer is currently running, pause it
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            startButton.setText("START");
            isRunning = false;
        }
    }


    private void formatSeconds(long millisUntilFinished) {

        long minutes_i = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;
        long seconds_i = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
        long hundredths_i = (millisUntilFinished % 1000) / 10; // Convert milliseconds to hundredths
        // Formatting the string to show seconds and hundredths of a second
        String min = String.format("%02d", minutes_i);
        String sec = String.format("%02d", seconds_i);
        String hun = String.format("%02d", hundredths_i);

        minutes.setText(min);
        seconds.setText(sec);
        hundredths.setText(hun);
    }

}
