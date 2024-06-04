package com.example.stillringstimer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import java.util.List;

public class RunRoutineActivity extends AppCompatActivity {

    private TextView workRestTextView;
    private TextView countdownTextView;
    private TextView nextUpTextView;
    private Button pauseButton;
    private Button resetButton;
    private CountDownTimer countDownTimer;
    private Gson gson;
    private Training training;
    private List<Interval> intervals;
    private int currentIntervalIndex = 0;
    private int currentRepetition = 0;
    private boolean isPaused = false;
    private long timeLeftInMillis;
    private boolean isWorkPhase;
    private MediaPlayer mediaPlayerStart;
    private MediaPlayer mediaPlayerStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_routine);
        ImageButton homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(RunRoutineActivity.this, Homepage.class);
            startActivity(intent);
        });
        ImageButton backButton = findViewById(R.id.goBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        gson = new Gson();

        workRestTextView = findViewById(R.id.workRestTextView);
        countdownTextView = findViewById(R.id.countdownTextView);
        nextUpTextView = findViewById(R.id.nextUpTextView);
        pauseButton = findViewById(R.id.pauseButton);
        resetButton = findViewById(R.id.resetButton);

        String trainingJson = getIntent().getStringExtra("selectedTraining");
        training = gson.fromJson(trainingJson, Training.class);
        intervals = training.getIntervals();

        pauseButton.setOnClickListener(v -> pauseResumeTimer());
        resetButton.setOnClickListener(v -> resetTimer());

        startNextInterval();
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

    @Override
    protected void onPause() {
        super.onPause();

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

    private void startNextInterval() {
        if (currentIntervalIndex < intervals.size()) {
            Interval currentInterval = intervals.get(currentIntervalIndex);
            currentRepetition = 0;
            isWorkPhase = true;
            timeLeftInMillis = currentInterval.getWorkDuration();
            workRestTextView.setText("WORK");
            nextUpTextView.setText("Next: Rest");

            startTimer();
        } else {
            // All intervals completed
            workRestTextView.setText("Done");
            countdownTextView.setText("00:00");
            nextUpTextView.setText("");
            pauseButton.setEnabled(false);
            resetButton.setEnabled(true);
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                Interval currentInterval = intervals.get(currentIntervalIndex);
                if (isWorkPhase) {
                    if (mediaPlayerStop == null) {
                        mediaPlayerStop = MediaPlayer.create(RunRoutineActivity.this, R.raw.finish); // Add a sound file to res/raw/finish.mp3
                    }
                    mediaPlayerStop.start();
                    isWorkPhase = false;
                    timeLeftInMillis = currentInterval.getRestDuration();
                    workRestTextView.setText("REST");
                    nextUpTextView.setText("Next: Work");
                } else {
                    if (mediaPlayerStart == null) {
                        mediaPlayerStart = MediaPlayer.create(RunRoutineActivity.this, R.raw.start); // Add a sound file to res/raw/start.mp3
                    }
                    mediaPlayerStart.start();
                    currentRepetition++;
                    if (currentRepetition < currentInterval.getRepetitions()) {
                        isWorkPhase = true;
                        timeLeftInMillis = currentInterval.getWorkDuration();
                        workRestTextView.setText("WORK");
                        nextUpTextView.setText("Next: Rest");
                    } else {
                        currentIntervalIndex++;
                        startNextInterval();
                        return;
                    }
                }
                startTimer();
            }
        }.start();

        pauseButton.setText("PAUSE");
    }

    private void pauseResumeTimer() {
        if (isPaused) {
            startTimer();
            isPaused = false;
            pauseButton.setText("PAUSE");
        } else {
            countDownTimer.cancel();
            isPaused = true;
            pauseButton.setText("RESUME");
        }
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        currentIntervalIndex = 0;
        isPaused = false;
        startNextInterval();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        countdownTextView.setText(timeFormatted);
    }
}
