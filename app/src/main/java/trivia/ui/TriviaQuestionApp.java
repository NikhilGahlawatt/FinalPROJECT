package trivia.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.databinding.ActivityTriviaQuestionAppBinding;

public class TriviaQuestionApp extends AppCompatActivity {

    private ActivityTriviaQuestionAppBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTriviaQuestionAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EditText usernameEditText = binding.userNameEditText;
        EditText categoryEditText = binding.categoryEditText;
        EditText numberOfQuestionsEditText = binding.numberOfQuestionsEditText;
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String username = prefs.getString("username","");
        String category = prefs.getString("category","");
        String numberOfQuestions = prefs.getString("numberOfQuestions","");
        usernameEditText.setText(username);
        categoryEditText.setText(category);
        numberOfQuestionsEditText.setText(numberOfQuestions);
        binding.loginButton.setOnClickListener( clk -> {
            editor.putString("username",usernameEditText.getText().toString());
            editor.putString("category",categoryEditText.getText().toString());
            editor.putString("numberOfQuestions",numberOfQuestionsEditText.getText().toString());
            editor.apply();
            Intent nextPage = new Intent(TriviaQuestionApp.this, QuestionsAttempt.class);
            nextPage.putExtra("username",usernameEditText.getText().toString());
            nextPage.putExtra("category",categoryEditText.getText().toString());
            nextPage.putExtra("numberOfQuestions",numberOfQuestionsEditText.getText().toString());
            startActivity(nextPage);
        });

    }
}
