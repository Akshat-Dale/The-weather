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
    TextView cityTextView,weatherConditionTextView,temperatureTextView,textViewSunriseTime,textViewSunsetTime,textViewWindSpeed,textViewHumidityPercentage;
    ImageView weatherImageView;
    LinearLayout linearLayoutFirst;
    LinearLayoutManager layoutManagerForRecycleView;
    RecyclerView recyclerViewForecast;
    String cityLatitude;
    String cityLongitude;
    String editTextCityName ,currentCity, currentTemperature, currentWeatherIconId,currentWindSpeed, currentWeatherDescriptionMain,currentHumidity,
            sunriseToday,sunsetToday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        finding id's
        cityEditText = findViewById(R.id.cityEditText);
        cityTextView = findViewById(R.id.cityTextView);
        weatherConditionTextView = findViewById(R.id.weatherConditionTextView);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        textViewSunsetTime = findViewById(R.id.textViewSunsetTime);
        textViewSunriseTime = findViewById(R.id.textViewSunriseTime);
        textViewWindSpeed = findViewById(R.id.textViewWindSpeed);
        textViewHumidityPercentage = findViewById(R.id.textViewHumidityPercentage);
        weatherImageView = findViewById(R.id.weatherImageView);
        linearLayoutFirst = findViewById(R.id.linearLayoutFirst);
        recyclerViewForecast = findViewById(R.id.recyclerViewForecast);
//
//        SETTING HORIZONTAL LIST LAYOUT
        layoutManagerForRecycleView = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false);
//        SETTING LAYOUT IN RECYCLER VIEW
        recyclerViewForecast.setLayoutManager(layoutManagerForRecycleView);
        recyclerViewForecast.setItemAnimator(new DefaultItemAnimator());
//        GETTING CITY NAME FROM EditText

    }







//    public void getLatLongCallAPI(View view){
//
//        RequestQueue requestQueue;
//        requestQueue = Volley.newRequestQueue(MainActivity.this);
//        Log.i("WEATHER_LATLONG",editTextCityName);
//        String LatLongURL ="https://api.openweathermap.org/geo/1.0/direct?q="+ editTextCityName +"&limit=1&appid=b63f3db52cd36ca5cc60f382ff723087";
//        JsonArrayRequest jsonArrayRequestLatLon = new JsonArrayRequest(Request.Method.GET, LatLongURL, null, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                Log.i("WEATHER_LATLONG",response.toString());
//
//                for (int i = 0; i < response.length(); i++) {
////                    if (!response.isNull(i)){
////                        getWeatherDataCallAPI();
////                    }else {
////                        Log.i("WEATHER_LATLONG","LAT LONG NOT FOUND");
////                    }
//
//                    try {
//                        JSONObject jsonObject = response.getJSONObject(i);
//                        String city = jsonObject.getString("name");
////                        SETTING CITY NAME ON SCREEN
//                         cityTextView.setText(city+"  ");
////                         GETTING LAT LONG
//                         cityLatitude = jsonObject.getString("lat");
//                         cityLongitude = jsonObject.getString("lon");
//
//                        Log.i("WEATHER_LATLONG", city);
//                        Log.i("WEATHER_LATLONG", cityLatitude);
//                        Log.i("WEATHER_LATLONG", cityLongitude);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("WEATHER",error.toString());
//            }
//        });
//        requestQueue.add(jsonArrayRequestLatLon);
//        GETTING WEATHER DATA THROW LAT LONG
//        if (cityLatitude != null && cityLongitude != null){
//       }
//    }


//    GETTING DATA CURRENT WEATHER DATA FROM API
    public void getWeatherDataCallAPI(View view){
//        GETTING CITY NAME FROM EDITTEXT
                editTextCityName = cityEditText.getText().toString();
//        GETTING WEATHER DATA THROW LAT LONG
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(MainActivity.this);
//        String getWeatherURL = "https://api.openweathermap.org/data/2.5/weather?lat="+cityLatitude+"&lon="+cityLongitude+
//                "&appid=b63f3db52cd36ca5cc60f382ff723087&units=metric";

        String getWeatherURL = "https://api.openweathermap.org/data/2.5/weather?q=" + editTextCityName + "&appid=b63f3db52cd36ca5cc60f382ff723087&units=metric";
        Log.d("WEATHER_DATA", "The URL is :" + getWeatherURL);

        JsonObjectRequest jsonObjectRequestGetWeather = new JsonObjectRequest(Request.Method.GET,
                getWeatherURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
//                    GETTING DATA FROM API AND SETTING TO SCREEN
                    currentTemperature = response.getJSONObject("main").getString("temp");
                    currentHumidity = response.getJSONObject("main").getString("humidity");
                    sunriseToday = response.getJSONObject("sys").getString("sunrise");
                    sunsetToday = response.getJSONObject("sys").getString("sunset");
                    currentCity = response.getString("name");
                    temperatureTextView.setText(String.format("%s°C", currentTemperature));
//                    GETTING FROM ARRAY "weather" TO OBJECT AND GET STRING FROM OBJECT AND SET SETTEXT
                     JSONArray jsonArray = response.getJSONArray("weather");
                     JSONObject jsonObject = jsonArray.getJSONObject(0);

                     currentWeatherDescriptionMain = jsonObject.getString("main");
                     currentWeatherIconId = jsonObject.getString("icon");
                     currentWindSpeed = response.getJSONObject("wind").getString("speed");
                     double windSpeedKM = ((Double.parseDouble(currentWindSpeed))*3.6 );
                     weatherConditionTextView.setText(currentWeatherDescriptionMain);
                     textViewWindSpeed.setText(windSpeedKM +" km/h ");
                     textViewHumidityPercentage.setText(currentHumidity + "% ");


                    Log.d("WEATHER_DATA", "The temperature is :" + currentTemperature);
                    Log.d("WEATHER_DATA", "The WeatherIconId is :" + currentWeatherIconId);
                    Log.d("WEATHER_DATA", "The Humidity is :" + currentHumidity);
                    Log.d("WEATHER_DATA", "The description is :" + currentWeatherDescriptionMain);
                    Log.d("WEATHER_DATA", "The wind is :" + windSpeedKM);
                    Log.d("WEATHER_DATA", "The Sunrise/Sunset is :" + sunriseToday+"/"+sunsetToday);
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