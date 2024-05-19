package com.example.stillringstimer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

public class DisplayTraining extends AppCompatActivity {
    private TextView trainingName;
    private LinearLayout intervalsContainer;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_training);

        ImageButton homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(DisplayTraining.this, Homepage.class);
            startActivity(intent);
        });
        gson = new Gson();

        trainingName = findViewById(R.id.routineName);
        intervalsContainer = findViewById(R.id.intervalsContainer);

        String trainingJson = getIntent().getStringExtra("selectedTraining");
        Training selectedTraining = gson.fromJson(trainingJson, Training.class);

        trainingName.setText(selectedTraining.getName());
        displayAllIntervals(selectedTraining.getIntervals());

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(DisplayTraining.this, RunRoutineActivity.class);
            intent.putExtra("selectedTraining", gson.toJson(selectedTraining));
            startActivity(intent);
        });
    }
    private void displayAllIntervals(List<Interval> intervals) {
        intervalsContainer.removeAllViews();
        for (Interval interval : intervals) {
            displayInterval(interval);
        }
    }
    private void displayInterval(Interval interval) {
        View intervalView = getLayoutInflater().inflate(R.layout.item_interval, intervalsContainer, false);
        TextView workDurationView = intervalView.findViewById(R.id.workDurationView);
        TextView restDurationView = intervalView.findViewById(R.id.restDurationView);
        TextView repetitionsView = intervalView.findViewById(R.id.repetitionsView);

        long workDurationSeconds = interval.getWorkDuration() / 1000L;
        long restDurationSeconds = interval.getRestDuration() / 1000L;
        int repetitions = interval.getRepetitions();

        workDurationView.setText(String.format("Work: %ds", workDurationSeconds));
        restDurationView.setText(String.format("Rest: %ds", restDurationSeconds));
        repetitionsView.setText(String.format("Reps: %d", repetitions));

        intervalsContainer.addView(intervalView);
    }
}