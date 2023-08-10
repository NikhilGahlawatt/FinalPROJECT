package trivia.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.databinding.ActivityResultBinding;
import algonquin.cst2335.finalproject.databinding.TriviaresultBinding;
import trivia.data.Response;
import trivia.data.ResponseDAO;
import trivia.data.ResponseDatabase;

public class Result extends AppCompatActivity {

    private ActivityResultBinding binding;
    RecyclerView.Adapter<MyRowHolder> myAdapter;
    ArrayList<String[]> responses;
    ResponseDAO rDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ResponseDatabase db = Room.databaseBuilder(getApplicationContext(), ResponseDatabase.class, "database-name").build();
        rDAO = db.rDAO();
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent fromPrevious = getIntent();
        responses = (ArrayList<String[]>) fromPrevious.getSerializableExtra("responses"); // Corrected naming here
        int Score = 0;
        for (int i =0; i<responses.size(); i++ ){
            String obj[] = responses.get(i);
            Toast.makeText(getApplicationContext(),obj[4],Toast.LENGTH_SHORT).show();
            Score = Score+Integer.parseInt(obj[4]);
        }
        String res1[] = responses.get(0);
        String username = res1[0];
        String category = res1[1];
        Response res = new Response();
        res.setUsername(username);
        res.setCategory(category);
        res.setScore(Score);

        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            rDAO.insertResponse(res);
        });
        binding.yourScores.setText(String.valueOf(Score));
        binding.response.setLayoutManager(new LinearLayoutManager(this));
        binding.response.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                TriviaresultBinding bind = TriviaresultBinding.inflate(getLayoutInflater(), parent, false);
                return new MyRowHolder(bind.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                String obj[] = responses.get(position);
                holder.resQuestion.setText("Q. " + obj[2]);
                holder.resAns.setText("Ans. " + obj[3]);
                holder.resScr.setText("Score: " + obj[4]);
            }

            @Override
            public int getItemCount() {
                return responses.size();
            }
        });
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView resAns;
        TextView resQuestion;
        TextView resScr;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            resQuestion = itemView.findViewById(R.id.resQues);
            resAns = itemView.findViewById(R.id.resAns);
            resScr = itemView.findViewById(R.id.resScore);
        }
    }
}