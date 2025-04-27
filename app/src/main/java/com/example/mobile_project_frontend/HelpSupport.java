package com.example.mobile_project_frontend;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HelpSupport extends AppCompatActivity {

    TextView Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9;
    TextView A1, A2, A3, A4, A5, A6, A7, A8, A9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_helpsupport);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Q1 = findViewById(R.id.question1);
        A1 = findViewById(R.id.answer1);
        Q2 = findViewById(R.id.question2);
        A2 = findViewById(R.id.answer2);
        Q3 = findViewById(R.id.question3);
        A3 = findViewById(R.id.answer3);
        Q4 = findViewById(R.id.question4);
        A4 = findViewById(R.id.answer4);
        Q5 = findViewById(R.id.question5);
        A5 = findViewById(R.id.answer5);
        Q6 = findViewById(R.id.question6);
        A6 = findViewById(R.id.answer6);
        Q7 = findViewById(R.id.question7);
        A7 = findViewById(R.id.answer7);
        Q8 = findViewById(R.id.question8);
        A8 = findViewById(R.id.answer8);
        Q9 = findViewById(R.id.question9);
        A9 = findViewById(R.id.answer9);

        Q1.setOnClickListener(view -> toggleAnswer(A1));
        Q2.setOnClickListener(view -> toggleAnswer(A2));
        Q3.setOnClickListener(view -> toggleAnswer(A3));
        Q4.setOnClickListener(view -> toggleAnswer(A4));
        Q5.setOnClickListener(view -> toggleAnswer(A5));
        Q6.setOnClickListener(view -> toggleAnswer(A6));
        Q7.setOnClickListener(view -> toggleAnswer(A7));
        Q8.setOnClickListener(view -> toggleAnswer(A8));
        Q9.setOnClickListener(view -> toggleAnswer(A9));
    }

    private void toggleAnswer(TextView answer) {
        if (answer.getVisibility() == View.GONE) {
            answer.setVisibility(View.VISIBLE);
        } else {
            answer.setVisibility(View.GONE);
        }
    }
}
