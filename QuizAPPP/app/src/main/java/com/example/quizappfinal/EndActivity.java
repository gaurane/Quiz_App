package com.example.quizappfinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class EndActivity extends AppCompatActivity {

    private TextView scoreText;
    private ImageView trophyImage;
    private Button restartButton;
    private TextView fullMarksText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);  // Ensure this is the correct layout file

        // Initialize views
        scoreText = findViewById(R.id.score_text);
        trophyImage = findViewById(R.id.trophy_image);
        restartButton = findViewById(R.id.restart_button);
        fullMarksText = findViewById(R.id.full_marks_text);

        // Get score and total from intent
        int score = getIntent().getIntExtra("SCORE", 0);
        int total = getIntent().getIntExtra("TOTAL", 0);

        // Display score and total
        scoreText.setText("Your Score: " + score + "/" + total);

        // Check if the score is perfect (full marks)
        if (score == total) {
            // Show the trophy image and full marks message if perfect score
            trophyImage.setVisibility(View.VISIBLE);
            fullMarksText.setVisibility(View.VISIBLE);

            // Apply fade-in animation for trophy
            Animation fadeIn = new AlphaAnimation(0.0f, 1.0f); // From transparent to fully visible
            fadeIn.setDuration(1000); // Duration of the fade-in (1 second)
            trophyImage.startAnimation(fadeIn);
            trophyImage.setImageResource(R.drawable.trophy);  // Set the trophy image

            // Fade-in animation for the "Yayy!" text
            fullMarksText.startAnimation(fadeIn);
        } else {
            // Hide the trophy and message if score is not full
            trophyImage.setVisibility(View.GONE);
            fullMarksText.setVisibility(View.GONE);
        }

        // Set up restart button to start a new quiz
        restartButton.setOnClickListener(v -> {
            // Restart the quiz or go back to the subject selection screen
            Intent intent = new Intent(EndActivity.this, SelectSubjectActivity.class);  // Change to your quiz subject selection activity
            startActivity(intent);
            finish();
        });
    }
}
