package trivia.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.R;
import algonquin.cst2335.finalproject.databinding.ActivityResultsBinding;
import algonquin.cst2335.finalproject.databinding.ResponseBinding;
import trivia.data.Response;
import trivia.data.ResponseDAO;
import trivia.data.ResponseDatabase;
import trivia.data.ResponseViewModel;

public class Results extends AppCompatActivity {

    private ActivityResultsBinding binding;
    ResponseViewModel responseModel ;
    ArrayList<Response> responsesData;
    ResponseDAO rDAO;
    RecyclerView.Adapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ResponseDatabase db = Room.databaseBuilder(getApplicationContext(), ResponseDatabase.class, "database-name").build();
        rDAO = db.rDAO();
        responseModel = new ViewModelProvider(this).get(ResponseViewModel.class);
        responsesData = responseModel.responsesData.getValue();
        if(responsesData == null)
        {
            responseModel.responsesData.setValue(responsesData = new ArrayList<>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                responsesData.addAll( rDAO.getAllResponses() ); //Once you get the data from database

                runOnUiThread( () ->  binding.responses.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }
        binding = ActivityResultsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.responses.setLayoutManager(new LinearLayoutManager(this));
        binding.responses.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                ResponseBinding bind = ResponseBinding.inflate(getLayoutInflater());
                return new MyRowHolder(bind.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                try {
                    Response response = responsesData.get(position);
                    holder.userSU2.setText(response.getUsername());
                    holder.uCategory2.setText(response.getCategory());
                    holder.uScore2.setText(String.valueOf(response.getScore()));
                    holder.btn4.setOnClickListener(clk -> {
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setMessage("Do you want to delete this response: " + response.getUsername())
                                .setTitle("Question: ")
                                .setNegativeButton("NO", (dialog, cl) -> { })
                                .setPositiveButton("Yes", (dialog, cl) -> {

                                    Response response1 = responsesData.get(position);
                                    responsesData.remove(position);
                                    myAdapter.notifyItemRemoved(position);

                                }).create().show();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public int getItemCount() {
                return responsesData.size();
            }
        });
    }
    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView userSU2;
        TextView uCategory2;
        TextView uScore2;
        Button btn4;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            userSU2 = itemView.findViewById(R.id.userS);
            uCategory2 = itemView.findViewById(R.id.uCategory);
            uScore2 = itemView.findViewById(R.id.uScore);
            btn4 = itemView.findViewById(R.id.triviaDelete);
        }
    }
}