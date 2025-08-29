package com.example.quizappfinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class SelectSubjectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_selection);

        Button androidBtn = findViewById(R.id.subject1Button);
        Button javaBtn = findViewById(R.id.subject2Button);
        Button kotlinBtn = findViewById(R.id.subject3Button);

        androidBtn.setOnClickListener(v -> openQuiz("Android"));
        javaBtn.setOnClickListener(v -> openQuiz("Java"));
        kotlinBtn.setOnClickListener(v -> openQuiz("Kotlin"));
    }

    private void openQuiz(String subject) {
        Intent intent = new Intent(SelectSubjectActivity.this, QuizActivity.class);
        intent.putExtra("SUBJECT", subject); // Pass subject to QuizActivity
        startActivity(intent);
    }
}
