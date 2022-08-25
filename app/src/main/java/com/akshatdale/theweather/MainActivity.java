package com.akshatdale.theweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    EditText cityEditText;
    TextView cityTextView,conditionTextView,temperatureTextView,sunTextView,windTextView;
    ImageView weatherImageView;
    LinearLayout linearLayoutFirst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        finding id's
        cityEditText = findViewById(R.id.cityEditText);
        cityTextView = findViewById(R.id.cityTextView);
        conditionTextView = findViewById(R.id.conditionTextView);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        sunTextView = findViewById(R.id.sunTextView);
        windTextView = findViewById(R.id.windTextView);
        weatherImageView = findViewById(R.id.weatherImageView);
        linearLayoutFirst = findViewById(R.id.linearLayoutFirst);


        Date dt = new Date();
        int hours = dt.getHours();
        System.out.println(hours);

        if (hours>=6 && hours<=19){
            weatherImageView.setImageResource(R.drawable.sun);
            linearLayoutFirst.setBackgroundResource(R.drawable.day);
        }
        else{
            weatherImageView.setImageResource(R.drawable.moon);
            linearLayoutFirst.setBackgroundResource(R.drawable.night);
        }

//        RequestQueue requestQueue;
//        requestQueue = Volley.newRequestQueue(this);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
//                "https://goweather.herokuapp.com/weather/indore", null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                try {
//                    Log.d("weather", "The temperature is :" + response.getString("temperature"));
//                    Log.d("weather", "The description is :" + response.getString("description"));
//                    Log.d("weather", "The wind is :" + response.getString("wind"));
//                } catch (JSONException e) {
//                    Log.i("weather","kuch lafda hai");
////                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("weather", "Something went wrong");
//            }
//        });
//
//        requestQueue.add(jsonObjectRequest);
//


    }
}