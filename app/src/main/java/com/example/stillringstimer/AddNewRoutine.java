package com.example.stillringstimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import android.content.SharedPreferences;


import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddNewRoutine extends AppCompatActivity {

    private EditText editRoutineName;
    private List<Interval> intervals;
    private LinearLayout intervalsContainer;
    private Training training;
    private Interval interval;
    private Button_up_down button_up_down;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_routine);

        intervals = new ArrayList<>();
        gson = new Gson();

        editRoutineName = findViewById(R.id.editRoutineName);
        Button saveRoutineButton = findViewById(R.id.saveRoutineButton);
        saveRoutineButton.setOnClickListener(v -> saveRoutine());

        ImageButton homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddNewRoutine.this, Homepage.class);
            startActivity(intent);
        });
        ImageButton backButton = findViewById(R.id.goBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        Button addIntervalButton = findViewById(R.id.addNewIntervalButton);
        addIntervalButton.setOnClickListener(v -> addNewInterval());

        intervalsContainer = findViewById(R.id.intervalsContainer);
    }
    private void saveRoutine() {
        String routineName = editRoutineName.getText().toString().trim();

        if (!routineName.isEmpty() && !intervals.isEmpty()) {
            Training training = new Training(routineName, intervals);
            saveTrainingToStorage(training);

            Toast.makeText(this, "Routine saved successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Routine name and at least one interval are required", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveTrainingToStorage(Training training) {
        SharedPreferences sharedPreferences = getSharedPreferences("trainings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String json = sharedPreferences.getString("trainings_list", "");
        Type type = new TypeToken<List<Training>>() {}.getType();
        List<Training> trainings = gson.fromJson(json, type);

        if (trainings == null) {
            trainings = new ArrayList<>();
        }

        trainings.add(training);
        json = gson.toJson(trainings);
        editor.putString("trainings_list", json);
        editor.apply();
    }

    private List<Training> getTrainingsFromStorage() {
        SharedPreferences sharedPreferences = getSharedPreferences("trainings", MODE_PRIVATE);
        String json = sharedPreferences.getString("trainings_list", "");
        Type type = new TypeToken<List<Training>>() {}.getType();
        List<Training> trainings = gson.fromJson(json, type);

        return trainings != null ? trainings : new ArrayList<>();
    }

    private void addNewInterval() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_interval, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Interval");
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        TextView workMinutesView = dialogView.findViewById(R.id.workMinutes);
        TextView workSecondsView = dialogView.findViewById(R.id.workSeconds);
        TextView workHundredthsView = dialogView.findViewById(R.id.workHundredths);

        TextView restMinutesView = dialogView.findViewById(R.id.restMinutes);
        TextView restSecondsView = dialogView.findViewById(R.id.restSeconds);
        TextView restHundredthsView = dialogView.findViewById(R.id.restHundredths);

        Button_up_down button_up_down_work = new Button_up_down(workSecondsView, workHundredthsView, true, workMinutesView);

        dialogView.findViewById(R.id.workMinutesUp).setOnClickListener(v -> button_up_down_work.minUp());
        dialogView.findViewById(R.id.workMinutesDown).setOnClickListener(v -> button_up_down_work.minDown());
        dialogView.findViewById(R.id.workSecondsUp).setOnClickListener(v -> button_up_down_work.secUp());
        dialogView.findViewById(R.id.workSecondsDown).setOnClickListener(v -> button_up_down_work.secDown());
        dialogView.findViewById(R.id.workHundredthsUp).setOnClickListener(v -> button_up_down_work.hunUp());
        dialogView.findViewById(R.id.workHundredthsDown).setOnClickListener(v -> button_up_down_work.hunDown());


        Button_up_down button_up_down_rest = new Button_up_down(restSecondsView, restHundredthsView, true, restMinutesView);

        dialogView.findViewById(R.id.restMinutesUp).setOnClickListener(v -> button_up_down_rest.minUp());
        dialogView.findViewById(R.id.restMinutesDown).setOnClickListener(v -> button_up_down_rest.minDown());
        dialogView.findViewById(R.id.restSecondsUp).setOnClickListener(v -> button_up_down_rest.secUp());
        dialogView.findViewById(R.id.restSecondsDown).setOnClickListener(v -> button_up_down_rest.secDown());
        dialogView.findViewById(R.id.restHundredthsUp).setOnClickListener(v -> button_up_down_rest.hunUp());
        dialogView.findViewById(R.id.restHundredthsDown).setOnClickListener(v -> button_up_down_rest.hunDown());

        TextView repetitionsView = dialogView.findViewById(R.id.repetitions);
        dialogView.findViewById(R.id.repetitionsUp).setOnClickListener(v -> updateRepetitions(repetitionsView, 1));
        dialogView.findViewById(R.id.repetitionsDown).setOnClickListener(v -> updateRepetitions(repetitionsView, -1));

        Button addIntervalButton = dialogView.findViewById(R.id.addIntervalButton);
        addIntervalButton.setOnClickListener(v -> {
            long workDuration = (Integer.parseInt(workMinutesView.getText().toString()) * 60000L) +
                    (Integer.parseInt(workSecondsView.getText().toString()) * 1000L) +
                    (Integer.parseInt(workHundredthsView.getText().toString()) * 10L);
            long restDuration = (Integer.parseInt(restMinutesView.getText().toString()) * 60000L) +
                    (Integer.parseInt(restSecondsView.getText().toString()) * 1000L) +
                    (Integer.parseInt(restHundredthsView.getText().toString()) * 10L);
            int repetitions = Integer.parseInt(repetitionsView.getText().toString());

            Interval interval = new Interval(workDuration, restDuration, repetitions);
            intervals.add(interval);
            displayInterval(interval);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void updateRepetitions(TextView view, int increment) {
        int repetitions = Integer.parseInt(view.getText().toString());
        repetitions = Math.max(0, repetitions + increment);
        view.setText(String.format("%02d", repetitions));
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

