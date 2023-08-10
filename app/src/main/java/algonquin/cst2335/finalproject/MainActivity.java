package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import algonquin.cst2335.finalproject.R;

import algonquin.cst2335.finalproject.CurrencyConvert.CurrencyActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button CurrencyBtn = findViewById(R.id.button2);
        CurrencyBtn.setOnClickListener( clk -> {
            startActivity(new Intent(MainActivity.this, CurrencyActivity.class));
        });


    }
}