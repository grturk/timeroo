package com.example.stillringstimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddNewRoutine extends AppCompatActivity {

    private EditText editRoutineName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_routine);


        editRoutineName = findViewById(R.id.editRoutineName);
        Button saveRoutineButton = findViewById(R.id.saveRoutineButton);
        saveRoutineButton.setOnClickListener(v -> saveRoutine());

        ImageButton homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(AddNewRoutine.this, Homepage.class);
            startActivity(intent);
        });
    }

    private void saveRoutine() {
        // dobim ime nove rutine iz Add routine
        String routineName = editRoutineName.getText().toString().trim();

        if (!routineName.isEmpty()) {
            // intent za vracanje imena, da ga lahko shranimo v list
            Intent resultIntent = new Intent();
            resultIntent.putExtra("newRoutineName", routineName);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        } else {
            Toast.makeText(this, "Routine name is required", Toast.LENGTH_SHORT).show();
        }
    }
}
