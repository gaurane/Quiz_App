package com.example.quizappfinal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class QuizActivity extends AppCompatActivity {

    private TextView questionNumber, timerText, questionText;
    private ProgressBar progressBar;
    private RadioGroup optionsGroup;
    private RadioButton option1, option2, option3, option4;
    private Button nextButton, previousButton;

    private int currentQuestionIndex = 0;
    private List<Question> currentQuestions;
    private String[] selectedAnswers;

    private android.os.CountDownTimer countDownTimer;
    private static final long TIME_PER_QUESTION = 10000; // 10 seconds


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Initialize views
        questionNumber = findViewById(R.id.questionNumber);
        timerText = findViewById(R.id.timerText);
        questionText = findViewById(R.id.questionText);
        progressBar = findViewById(R.id.progressBar);
        optionsGroup = findViewById(R.id.optionsGroup);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.prevButton);

        // Get the selected subject passed from the previous activity
        String subject = getIntent().getStringExtra("SUBJECT");
        if (subject != null) {
            loadQuestionsFromJson(subject);
        } else {
            Toast.makeText(this, "Subject not selected", Toast.LENGTH_SHORT).show();
            finish(); // End activity if no subject is passed
        }

        // Set up next button listener
        nextButton.setOnClickListener(v -> {
            saveSelectedAnswer();
            if (currentQuestionIndex < currentQuestions.size() - 1) {
                currentQuestionIndex++;
                loadQuestion();
            } else {
                int score = calculateScore();
                Intent intent = new Intent(QuizActivity.this, EndActivity.class);
                intent.putExtra("SCORE", score);
                intent.putExtra("TOTAL", currentQuestions.size());
                startActivity(intent);
                finish();
            }
        });

        // Set up previous button listener
        previousButton.setOnClickListener(v -> {
            if (currentQuestionIndex > 0) {
                saveSelectedAnswer();
                currentQuestionIndex--;
                loadQuestion();
            }
        });

        // Load the first question
        loadQuestion();
    }

    private void loadQuestionsFromJson(String subject) {
        try {
            // Read the JSON file from assets
            InputStream inputStream = getAssets().open("questions.json");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer);

            // Parse JSON into a map with subjects and questions
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<Question>>>() {}.getType();
            Map<String, List<Question>> allQuestions = gson.fromJson(json, type);

            // Get the questions for the selected subject
            currentQuestions = allQuestions.get(subject);

            if (currentQuestions == null || currentQuestions.isEmpty()) {
                throw new Exception("No questions found for subject: " + subject);
            }

            selectedAnswers = new String[currentQuestions.size()]; // Initialize array to track selected answers

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading questions: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadQuestion() {
        if (currentQuestions != null && currentQuestionIndex < currentQuestions.size()) {
            Question currentQuestion = currentQuestions.get(currentQuestionIndex);
            questionNumber.setText("Question " + (currentQuestionIndex + 1));
            progressBar.setProgress((int)(((double)(currentQuestionIndex + 1) / currentQuestions.size()) * 100));
            questionText.setText(currentQuestion.getQuestionText());

            // Set options for the question
            option1.setText(currentQuestion.getOptions().get(0));
            option2.setText(currentQuestion.getOptions().get(1));
            option3.setText(currentQuestion.getOptions().get(2));
            option4.setText(currentQuestion.getOptions().get(3));

            optionsGroup.clearCheck(); // Clear previous selection

            // Restore previous selection if available
            String savedAnswer = selectedAnswers[currentQuestionIndex];
            if (savedAnswer != null) {
                if (option1.getText().toString().equals(savedAnswer)) option1.setChecked(true);
                else if (option2.getText().toString().equals(savedAnswer)) option2.setChecked(true);
                else if (option3.getText().toString().equals(savedAnswer)) option3.setChecked(true);
                else if (option4.getText().toString().equals(savedAnswer)) option4.setChecked(true);
            }
            // Start or reset the timer
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }

            countDownTimer = new android.os.CountDownTimer(TIME_PER_QUESTION, 1000) {
                public void onTick(long millisUntilFinished) {
                    timerText.setText("Time: " + (millisUntilFinished / 1000) + "s");
                }

                public void onFinish() {
                    timerText.setText("Time's up!");
                    saveSelectedAnswer();
                    if (currentQuestionIndex < currentQuestions.size() - 1) {
                        currentQuestionIndex++;
                        loadQuestion();
                    } else {
                        int score = calculateScore();
                        Intent intent = new Intent(QuizActivity.this, EndActivity.class);
                        intent.putExtra("SCORE", score);
                        intent.putExtra("TOTAL", currentQuestions.size());
                        startActivity(intent);
                        finish();
                    }
                }
            }.start();
        }

    }

    private void saveSelectedAnswer() {
        int selectedId = optionsGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            String selectedAnswer = selectedRadioButton.getText().toString();
            selectedAnswers[currentQuestionIndex] = selectedAnswer;
        }
    }

    private int calculateScore() {
        int score = 0;
        for (int i = 0; i < currentQuestions.size(); i++) {
            String selected = selectedAnswers[i];
            if (selected != null && selected.equals(currentQuestions.get(i).getCorrectAnswer())) {
                score++;
            }
        }
        return score;
    }
}

// Question class to represent the data structure of each question
class Question {
    private String questionText;
    private List<String> options;
    private String correctAnswer;

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
