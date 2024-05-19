package com.example.stillringstimer;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DisplayTraining extends AppCompatActivity {
    private TextView trainingName;
    private LinearLayout intervalsContainer;
    private Gson gson;

    private List<Training> trainings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_training);

        ImageButton homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(DisplayTraining.this, Homepage.class);
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


        trainings = getTrainingsFromStorage();

        ImageButton deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> {
            deleteTraining(selectedTraining);
            Toast.makeText(this, "Training deleted", Toast.LENGTH_SHORT).show();
            finish();  // Close the activity after deletion
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

    private void deleteTraining(Training selectedTraining) {
        trainings.removeIf(training -> training.getName().equals(selectedTraining.getName()));
        saveTrainingsToStorage(trainings);
    }

    private List<Training> getTrainingsFromStorage() {
        SharedPreferences sharedPreferences = getSharedPreferences("trainings", MODE_PRIVATE);
        String json = sharedPreferences.getString("trainings_list", "");
        Type type = new TypeToken<List<Training>>() {}.getType();
        List<Training> trainings = gson.fromJson(json, type);

        return trainings != null ? trainings : new ArrayList<>();
    }

    private void saveTrainingsToStorage(List<Training> trainings) {
        SharedPreferences sharedPreferences = getSharedPreferences("trainings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(trainings);
        editor.putString("trainings_list", json);
        editor.apply();
    }
}