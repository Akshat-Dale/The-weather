package com.akshatdale.theweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText cityEditText;
    TextView cityTextView,conditionTextView,temperatureTextView,sunriseTextView,windTextView;
    ImageView weatherImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityEditText = findViewById(R.id.cityEditText);
        cityTextView = findViewById(R.id.cityTextView);
        conditionTextView = findViewById(R.id.conditionTextView);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        sunriseTextView = findViewById(R.id.sunriseTextView);
        windTextView = findViewById(R.id.windTextView);
        weatherImageView = findViewById(R.id.weatherImageView);
    }
}