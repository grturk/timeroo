package com.example.stillringstimer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MyRoutines extends AppCompatActivity {


    private RecyclerView routinesRecyclerView;
    private List<Training> trainings;
    private Gson gson;
    private RoutineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_routines);

        gson = new Gson();
        routinesRecyclerView = findViewById(R.id.routinesRecyclerView);
        routinesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        trainings = getTrainingsFromStorage();
        adapter = new RoutineAdapter(trainings, this);
        routinesRecyclerView.setAdapter(adapter);

        Button addNewActivityButton = findViewById(R.id.addNewActivityButton);
        addNewActivityButton.setOnClickListener(v -> {
            Intent intent = new Intent(MyRoutines.this, AddNewRoutine.class);
            startActivity(intent);
        });
        ImageButton homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MyRoutines.this, Homepage.class);
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

    private List<Training> getTrainingsFromStorage() {
        SharedPreferences sharedPreferences = getSharedPreferences("trainings", MODE_PRIVATE);
        String json = sharedPreferences.getString("trainings_list", "");
        Type type = new TypeToken<List<Training>>() {}.getType();
        List<Training> trainings = gson.fromJson(json, type);

        return trainings != null ? trainings : new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the list of trainings when returning to this activity
        trainings.clear();
        trainings.addAll(getTrainingsFromStorage());
        adapter.notifyDataSetChanged();
    }
}