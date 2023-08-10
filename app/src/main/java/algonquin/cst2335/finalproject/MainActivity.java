package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import algonquin.cst2335.finalproject.CurrencyConvert.CurrencyActivity;
import algonquin.cst2335.finalproject.R;

import algonquin.cst2335.finalproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding MainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(MainBinding.getRoot());

        Button aviation_Button = MainBinding.button;
        Button CurrencyBtn = MainBinding.button2;

        aviation_Button.setOnClickListener( clk-> {
          Intent aviationPage = new Intent( MainActivity.this, AviationActivity.class);
            startActivity( aviationPage);
        } );

        CurrencyBtn.setOnClickListener( clk -> {
            startActivity(new Intent(MainActivity.this, CurrencyActivity.class));
        });



    }
}