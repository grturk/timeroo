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

    private long startTime;
    private TextView seconds;
    private TextView hundredths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> startTimer());

        seconds = findViewById(R.id.seconds);
        hundredths = findViewById(R.id.hundredths);
        Button seconds_up = findViewById(R.id.seconds_up_btn);
        Button seconds_down = findViewById(R.id.seconds_down_btn);
        Button hundredths_up = findViewById(R.id.hundredths_up_btn);
        Button hundredths_down = findViewById(R.id.hundredths_down_btn);

        int[] time = getTimeFromView();
        startTime = time[0] * 1000L + time[1] * 10;

        seconds_up.setOnClickListener(v -> secUp());
        seconds_down.setOnClickListener(v -> secDown());
        hundredths_up.setOnClickListener(v -> hunUp());
        hundredths_down.setOnClickListener(v -> hunDown());

        ImageButton homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Homepage.class);
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

    private void secUp() {
        int[] time = getTimeFromView();
        int sec = time[0];
        int hun = time[1];

        if(sec < 59) {
            sec++;
        } else {
            sec = 0;
        }

        startTime = sec * 1000L + hun * 10;

        String sec_string = String.format("%02d", sec);
        seconds.setText(sec_string);
    }
    private void secDown() {
        int[] time = getTimeFromView();
        int sec = time[0];
        int hun = time[1];

        if(sec > 0) {
            sec--;
        } else {
            sec = 59;
        }

        startTime = sec * 1000L + hun * 10;

        String sec_string = String.format("%02d", sec);
        seconds.setText(sec_string);
    }
    private void hunUp() {
        int[] time = getTimeFromView();
        int sec = time[0];
        int hun = time[1];

        if(hun == 0) {
            hun += 50;
        } else if(sec >= 59){
            sec = 0;
            hun = 0;
        } else {
            hun = 0;
            sec += 1;
        }

        startTime = sec * 1000L + hun * 10;

        String sec_string = String.format("%02d", sec);
        String hun_string = String.format("%02d", hun);
        seconds.setText(sec_string);
        hundredths.setText(hun_string);
    }
    private void hunDown() {
        int[] time = getTimeFromView();
        int sec = time[0];
        int hun = time[1];

        if(hun == 50) {
            hun -= 50;
        } else if (sec == 0){
            sec = 59;
            hun = 50;
        } else {
            hun = 50;
            sec -= 1;
        }

        startTime = sec * 1000L + hun * 10;

        String sec_string = String.format("%02d", sec);
        String hun_string = String.format("%02d", hun);
        seconds.setText(sec_string);
        hundredths.setText(hun_string);
    }

    private int[] getTimeFromView() {

        String seconds_string = seconds.getText().toString();
        int seconds_input = Integer.parseInt(seconds_string);

        String hundredths_string = hundredths.getText().toString();
        int hundredths_input = Integer.parseInt(hundredths_string);
        int[] t = new int[]{seconds_input, hundredths_input};

        return t;
    }
    private void startTimer() {

        if (mediaPlayerStart == null) {
            mediaPlayerStart = MediaPlayer.create(MainActivity.this, R.raw.start); // Add a sound file to res/raw/sound_file.mp3
        }
        mediaPlayerStart.start();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(startTime, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                formatSeconds(millisUntilFinished);
            }

            @Override
            public void onFinish() {

                formatSeconds(startTime);
                // Play a sound

                if (mediaPlayerStop == null) {
                    mediaPlayerStop = MediaPlayer.create(MainActivity.this, R.raw.finish); // Add a sound file to res/raw/sound_file.mp3
                }
                mediaPlayerStop.start();

            }



        }.start();
    }

    private void formatSeconds(long millisUntilFinished) {

        long seconds_i = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60;
        long hundredths_i = (millisUntilFinished % 1000) / 10; // Convert milliseconds to hundredths
        // Formatting the string to show seconds and hundredths of a second
        String sec = String.format("%02d", seconds_i);
        String hun = String.format("%02d", hundredths_i);

        seconds.setText(sec);
        hundredths.setText(hun);
    }

}