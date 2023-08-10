package trivia.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;

import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.databinding.ActivityQuestionsAttemptBinding;
import algonquin.cst2335.finalproject.databinding.QuestionsBinding;
import trivia.data.Question;
import trivia.data.QuestionAttemptViewModel;

public class QuestionsAttempt extends AppCompatActivity {

    private ActivityQuestionsAttemptBinding binding;
    QuestionAttemptViewModel questionModel;
    ArrayList<Question> questions;
    ArrayList<String> correctAnswers;
    ArrayList<String[]> responses;
    RecyclerView.Adapter<MyRowHolder> myAdapter;
    RequestQueue queue = null;
    String usernamecurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionsAttemptBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        queue = Volley.newRequestQueue(this);
        questionModel = new ViewModelProvider(this).get(QuestionAttemptViewModel.class);
        questions = questionModel.questions.getValue();
        correctAnswers = questionModel.correctAnswers.getValue();
        responses = new ArrayList<>();
        if (questions == null) {
            questions = new ArrayList<>();
            questionModel.questions.postValue(questions);
        }
        if (correctAnswers == null) {
            correctAnswers = new ArrayList<>();
            questionModel.correctAnswers.postValue(correctAnswers);
        }
        Intent fromPrevious = getIntent();
        String username = fromPrevious.getStringExtra("username");
        String password = fromPrevious.getStringExtra("password");
        String category = fromPrevious.getStringExtra("category");
        String numberOfQuestions = fromPrevious.getStringExtra("numberOfQuestions");
        usernamecurrent = username;
        binding.usernameAT.setText(username);
        binding.numberOfQuestionsAT.setText(numberOfQuestions);

        String stringURL;
        try {
            stringURL = "https://opentdb.com/api.php?amount=" + URLEncoder.encode(numberOfQuestions, "UTF-8") + "&category=" + URLEncoder.encode(category, "UTF-8") + "&type=multiple";
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                (response) -> {
                    try {
                        JSONArray result = response.getJSONArray("results");
                        int position;
                        for (position = 0; position < result.length(); position++) {
                            JSONObject obj = result.getJSONObject(position);
                            String categoryAPI = obj.getString("category");
                            String questionAPI = obj.getString("question");
                            String option1API = obj.getString("correct_answer");
                            JSONArray objOptions = obj.getJSONArray("incorrect_answers");
                            String option2API = objOptions.getString(0);
                            String option3API = objOptions.getString(1);
                            String option4API = objOptions.getString(2);
                            correctAnswers.add(position, option1API);
                            if (position == 0) {
                                binding.categoryTypeAT.setText(categoryAPI);
                            }
                            runOnUiThread(() -> {
                                String categoryq = categoryAPI;
                                String questionq = questionAPI;
                                String option1 = option1API;
                                String option2 = option2API;
                                String option3 = option3API;
                                String option4 = option4API;
                                addArrayList(categoryq, questionq, option1, option2, option3, option4);
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                (error) -> {
                });
        queue.add(request);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                QuestionsBinding bindingQ = QuestionsBinding.inflate(getLayoutInflater());
                return new MyRowHolder(bindingQ.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                try {
                    Question question = questions.get(position);
                    holder.serialNumber.setText(String.valueOf(position + 1));
                    holder.question.setText(question.getQuestion());
                    holder.option1.setText(question.getOption1());
                    holder.option2.setText(question.getOption2());
                    holder.option3.setText(question.getOption3());
                    holder.option4.setText(question.getOption4());
                    holder.answers.setOnCheckedChangeListener((group, checkedId) -> {
                        int checkedPosition = holder.getAdapterPosition();
                        if (checkedPosition != RecyclerView.NO_POSITION) {
                            Question selectedQuestion = questions.get(checkedPosition);
                            int selectedAnswerId = holder.answers.getCheckedRadioButtonId();
                            RadioButton selectedAnswer = holder.itemView.findViewById(selectedAnswerId);
                            String score;
                            String username = null;
                            String category = null;
                            if (checkedPosition == 0) {
                                username = usernamecurrent;
                                category = selectedQuestion.getCategory();
                            } else {
                                username = "";
                                category="";
                            }
                            if (selectedAnswer != null && selectedAnswer.getText().toString().equals(correctAnswers.get(checkedPosition))) {
                                score = "1";
                            } else {
                                score = "0";
                            }
                            String rspnnss[] ={username, category, selectedQuestion.getQuestion(), selectedAnswer.getText().toString(), score};
                            responses.add(rspnnss);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public int getItemCount() {
                return questions.size();
            }
        });

        binding.submitButton.setOnClickListener(clk -> {
            Intent resultPage = new Intent(QuestionsAttempt.this, Result.class);
            resultPage.putExtra("responses", responses);
            startActivity(resultPage);
        });
        binding.showResultsButton.setOnClickListener(click->{
            Intent showResult = new Intent(QuestionsAttempt.this, Results.class);
            startActivity(showResult);
        });
    }

    private void addArrayList(String categoryq, String questionq, String option1, String option2, String option3, String option4) {
        HashSet<String> uniqueValues = new HashSet<>();
        Question question = new Question(categoryq,questionq,option1,option2,option3,option4);
        questions.add(question);
        myAdapter.notifyItemInserted(questions.size()-1);
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView serialNumber;
        TextView question;
        RadioGroup answers;
        RadioButton option1;
        RadioButton option2;
        RadioButton option3;
        RadioButton option4;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            serialNumber = itemView.findViewById(R.id.serialNumber);
            question = itemView.findViewById(R.id.question);
            answers = itemView.findViewById(R.id.answers);
            option1 = itemView.findViewById(R.id.option1);
            option2 = itemView.findViewById(R.id.option2);
            option3 = itemView.findViewById(R.id.option3);
            option4 = itemView.findViewById(R.id.option4);
        }
    }
}