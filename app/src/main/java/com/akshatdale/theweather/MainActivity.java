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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
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

//        GETTING CITY NAME FROM EditText
        String GetCityName = cityEditText.getText().toString();
//
    }







    public void callWeatherAPI(){
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(MainActivity.this);

        String getCityLatLong ="https://api.openweathermap.org/geo/1.0/direct?q=khirkiya,&limit=1&appid=b63f3db52cd36ca5cc60f382ff723087";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, getCityLatLong, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray profile = null;
                try {

                    profile = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject jresponse = profile.getJSONObject(0);
                    Log.i("WEATHER", "The CITY is :" + response.getString(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                getCityLatLong, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.d("WEATHER", "The CITY is :" + response.getString("name"));
                } catch (JSONException e) {
                    Log.i("WEATHER","kuch lafda hai");
//                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("weather", "Something went wrong");
            }
        });
         requestQueue.add(jsonObjectRequest);

    }
}