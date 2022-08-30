package com.akshatdale.theweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {

    EditText cityEditText;
    TextView cityTextView,weatherConditionTextView,temperatureTextView,sunTextView,windTextView;
    ImageView weatherImageView;
    LinearLayout linearLayoutFirst;
    LinearLayoutManager layoutManagerForRecycleView;
    RecyclerView recyclerViewForecast;
    String editTextCityName;
    String cityLatitude;
    String cityLongitude;
    String currentTemperature;
    String windSpeed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        finding id's
        cityEditText = findViewById(R.id.cityEditText);
        cityTextView = findViewById(R.id.cityTextView);
        weatherConditionTextView = findViewById(R.id.weatherConditionTextView);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        sunTextView = findViewById(R.id.sunTextView);
        windTextView = findViewById(R.id.windTextView);
        weatherImageView = findViewById(R.id.weatherImageView);
        linearLayoutFirst = findViewById(R.id.linearLayoutFirst);
        recyclerViewForecast = findViewById(R.id.recyclerViewForecast);
//        SETTING HORIZONTAL LIST LAYOUT
        layoutManagerForRecycleView = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false);
//        SETTING LAYOUT IN RECYCLER VIEW
        recyclerViewForecast.setLayoutManager(layoutManagerForRecycleView);
        recyclerViewForecast.setItemAnimator(new DefaultItemAnimator());
//        GETTING CITY NAME FROM EditText

    }







    public void getLatLongCallAPI(View view){
        editTextCityName = cityEditText.getText().toString();
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        Log.i("WEATHER_LATLONG",editTextCityName);
        String LatLongURL ="https://api.openweathermap.org/geo/1.0/direct?q="+ editTextCityName +"&limit=1&appid=b63f3db52cd36ca5cc60f382ff723087";
        JsonArrayRequest jsonArrayRequestLatLon = new JsonArrayRequest(Request.Method.GET, LatLongURL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("WEATHER_LATLONG",response.toString());

                for (int i = 0; i < response.length(); i++) {
//                    if (!response.isNull(i)){
//                        getWeatherDataCallAPI();
//                    }else {
//                        Log.i("WEATHER_LATLONG","LAT LONG NOT FOUND");
//                    }

                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String city = jsonObject.getString("name");
//                        SETTING CITY NAME ON SCREEN
                         cityTextView.setText(city+"  ");
//                         GETTING LAT LONG
                         cityLatitude = jsonObject.getString("lat");
                         cityLongitude = jsonObject.getString("lon");

                        Log.i("WEATHER_LATLONG", city);
                        Log.i("WEATHER_LATLONG", cityLatitude);
                        Log.i("WEATHER_LATLONG", cityLongitude);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("WEATHER",error.toString());
            }
        });
        requestQueue.add(jsonArrayRequestLatLon);
//        GETTING WEATHER DATA THROW LAT LONG
//        if (cityLatitude != null && cityLongitude != null){
//       }

    }

    public void getWeatherDataCallAPI(View view){
//        GETTING WEATHER DATA THROW LAT LONG
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        String getWeatherURL = "https://api.openweathermap.org/data/2.5/weather?lat="+cityLatitude+"&lon="+cityLongitude+
                "&appid=b63f3db52cd36ca5cc60f382ff723087&units=metric";
        Log.d("WEATHER_DATA", "The URL is :" + getWeatherURL);

        JsonObjectRequest jsonObjectRequestGetWeather = new JsonObjectRequest(Request.Method.GET,
                getWeatherURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
//                    GETTING DATA FROM API AND SETTING TO SCREEN
                    currentTemperature = response.getJSONObject("main").getString("temp");
                    temperatureTextView.setText(currentTemperature+"°C ");
//                    GETTING FROM ARRAY TO OBJECT AND GET STRING FROM OBJECT AND SET SETTEXT
                     JSONArray jsonArray = response.getJSONArray("weather");
                     JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String weatherDescription = jsonObject.getString("main");
                    weatherConditionTextView.setText(weatherDescription+"  ");
//                    GET WIND SPEED AND MULTIPLY WITH 3.6 TO CONVER KM/H
                    windSpeed = response.getJSONObject("wind").getString("speed");
                    double windSpeedKM = ((Double.parseDouble(windSpeed))*3.6 );
                    windTextView.setText(String.valueOf(windSpeedKM)+" km/h ");

                    Log.d("WEATHER_DATA", "The temperature is :" + currentTemperature);
                    Log.d("WEATHER_DATA", "The description is :" + weatherDescription);
                    Log.d("WEATHER_DATA", "The wind is :" + windSpeed);
                } catch (JSONException e) {
                    Log.i("WEATHER_DATA","SOME ERROR IN RESPONSE WEATHER");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("WEATHER_DATA", "SOMETHING WENT WRONG IN WEATHER DATA");
            }
        });

        requestQueue.add(jsonObjectRequestGetWeather);
    }
}