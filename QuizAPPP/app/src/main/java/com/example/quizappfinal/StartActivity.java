package com.example.quizappfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    private Button startButton;
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Initialize Views
        startButton = findViewById(R.id.startButton);
        welcomeText = findViewById(R.id.welcomeText);

        // Apply Slide-Up Animation to Button
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        startButton.startAnimation(slideUp);

        // Start Quiz on Button Click
        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, SelectSubjectActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }
}
