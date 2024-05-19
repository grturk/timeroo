package com.example.stillringstimer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

/*
public class MyRoutines extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_ROUTINE = 1;
    ArrayList<intervalsModel> routinesList = new ArrayList<>();
    private RoutinesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_routines);

        // nardila recycler view namesto basic lista
        RecyclerView recyclerView = findViewById(R.id.routinesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RoutinesAdapter(routinesList);
        recyclerView.setAdapter(adapter);

        ImageButton homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MyRoutines.this, Homepage.class);
            startActivity(intent);
        });

        Button plusButton = findViewById(R.id.addNewActivityButton);
        plusButton.setOnClickListener(v -> {
            Intent intent = new Intent(MyRoutines.this, AddNewRoutine.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_ROUTINE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MyRoutines", "onActivityResult called");
        // preverimo, ce smo dobili podatke od nove rutine
        if (requestCode == REQUEST_CODE_ADD_ROUTINE && resultCode == Activity.RESULT_OK) {
                String newRoutineName = data.getStringExtra("newRoutineName");
                if (newRoutineName != null) {
                    //klicemo, da dodamo nov interval v obliki intervalModel v list
                    setUpIntervalsModels(newRoutineName);
                }
        }
    }


    private void setUpIntervalsModels(String newRoutineName) {
        intervalsModel newInterval = new intervalsModel(newRoutineName);
        routinesList.add(newInterval);
        adapter.notifyDataSetChanged();
    }

}
*/

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