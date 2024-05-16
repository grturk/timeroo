package com.example.stillringstimer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Homepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button choose_classic = findViewById(R.id.secondGreenButton);
        choose_classic.setOnClickListener(v -> {
            Intent intent = new Intent(Homepage.this, MainActivity.class);
            startActivity(intent);
        });

        Button choose_intervals = findViewById(R.id.thirdGreenButton);
        choose_intervals.setOnClickListener(v -> {
            Intent intent = new Intent(Homepage.this, MyRoutines.class);
            startActivity(intent);
        });


    }
}