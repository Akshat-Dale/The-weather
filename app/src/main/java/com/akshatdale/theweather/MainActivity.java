package com.akshatdale.theweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final DecimalFormat df = new DecimalFormat("0.0");
    EditText cityEditText;
    TextView cityTextView, weatherConditionTextView, temperatureTextView, textViewSunriseTime, textViewSunsetTime, textViewWindSpeed, textViewHumidityPercentage;
    ImageView weatherImageView;
    LinearLayout linearLayoutFirst;
    LinearLayoutManager layoutManagerForRecycleView;
    RecyclerView recyclerViewForecast;
    String cityLatitude;
    String cityLongitude;
    String editTextCityName, currentCity, currentTemperature, currentWeatherIconId, currentWindSpeed, currentWeatherDescriptionMain, currentHumidity,
            sunriseToday, sunsetToday;

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
        layoutManagerForRecycleView = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
//        SETTING LAYOUT IN RECYCLER VIEW
        recyclerViewForecast.setLayoutManager(layoutManagerForRecycleView);
        recyclerViewForecast.setItemAnimator(new DefaultItemAnimator());
//        GETTING CITY NAME FROM EditText

    }


    //    GETTING DATA CURRENT WEATHER DATA FROM API
    public void getWeatherDataCallAPI(View view) {
//        GETTING CITY NAME FROM EDITTEXT
        editTextCityName = cityEditText.getText().toString();
//        GETTING WEATHER DATA THROW LAT LONG
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(MainActivity.this);

        String getWeatherURL = "https://api.openweathermap.org/data/2.5/weather?q=" + editTextCityName + "&appid=b63f3db52cd36ca5cc60f382ff723087&units=metric";
        Log.d("WEATHER_apiCall", "The URL is :" + getWeatherURL);

        JsonObjectRequest jsonObjectRequestGetWeather = new JsonObjectRequest(Request.Method.GET,
                getWeatherURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
//                    GETTING DATA FROM API AND SETTING TO SCREEN
                    currentTemperature = response.getJSONObject("main").getString("temp");
                    double temperatureDouble = Double.parseDouble(currentTemperature);
                    String currentTemperatureDigit = df.format(temperatureDouble);
                    currentHumidity = response.getJSONObject("main").getString("humidity");
                    sunriseToday = response.getJSONObject("sys").getString("sunrise");
                    sunsetToday = response.getJSONObject("sys").getString("sunset");
                    currentCity = response.getString("name");
                    temperatureTextView.setText(String.format("%s°C ", currentTemperatureDigit));
                    textViewHumidityPercentage.setText(currentHumidity + "% ");
                    cityTextView.setText(currentCity + "  ");
//                     SEND TO isConverter METHOD TO GET TIME IN IST
                    textViewSunriseTime.setText(istConverter(sunriseToday));
                    textViewSunsetTime.setText(istConverter(sunsetToday));
//                    GETTING FROM ARRAY "weather" TO OBJECT AND GET STRING FROM OBJECT AND SET SETTEXT
                    JSONArray jsonArray = response.getJSONArray("weather");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    currentWeatherDescriptionMain = jsonObject.getString("main");
                    currentWeatherIconId = jsonObject.getString("icon");
                    currentWindSpeed = response.getJSONObject("wind").getString("speed");

                    double windSpeedKM = ((Double.parseDouble(currentWindSpeed)) * 3.6);
                    String windSpeed2Digit = df.format(windSpeedKM);

                    weatherConditionTextView.setText(currentWeatherDescriptionMain + "  ");
                    textViewWindSpeed.setText(windSpeed2Digit + " km/h");

                } catch (JSONException e) {
                    Log.i("WEATHER_apiCall", "SOME ERROR IN RESPONSE WEATHER");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("WEATHER_apiCall", "SOMETHING WENT WRONG IN WEATHER DATA");
            }
        });

        requestQueue.add(jsonObjectRequestGetWeather);
//        CALLING ANOTHER API PARALLEL TO GET FORECAST DATA
        getForecastWeatherData();
    }

    //    CONVERT UNIX TIME FORMAT TO IST TIME FORMAT
    public String istConverter(String seconds) {
        // Unix seconds
        long unix_seconds = Long.parseLong(seconds);
        // convert seconds to milliseconds
        Date date = new Date(unix_seconds * 1000L);
        // format of the date
        @SuppressLint("SimpleDateFormat") SimpleDateFormat jdf = new SimpleDateFormat("HH:mm");
        String java_date = jdf.format(date);
        System.out.println("\n" + java_date + "\n");

        return java_date;
    }

    public void getForecastWeatherData(){

        String forecastWeatherUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + editTextCityName+ "&appid=b63f3db52cd36ca5cc60f382ff723087&units=metric";
        Log.i("WEATHER_DATA_Forecast",forecastWeatherUrl);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, forecastWeatherUrl,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i("WEATHER_DATA_Forecast", (String) response.get("cod"));
                    JSONArray jsonArray = response.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++) {


                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String dateTime = istConverter(jsonObject.getString("dt"));
                        String temperature = jsonObject.getJSONObject("main").getString("temp");
                        double temperatureDouble = Double.parseDouble(temperature);
                        String temperatureDigit = df.format(temperatureDouble);
                        Log.i("WEATHER_DATA_Forecast", "Date " + dateTime + " Temp. " + temperatureDigit);

                        JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                        String descriptionMain = jsonObjectWeather.getString("main");
                        String description = jsonObjectWeather.getString("description");
                        String iconId = jsonObjectWeather.getString("icon");
                        Log.i("WEATHER_DATA_Forecast", "Description " + descriptionMain + " IconId. " + iconId +" Descrition  " +description);
                    }
                } catch (JSONException e) {
                    Log.e("WEATHER_DATA_Forecast","SOME ERROR TO GET DATA FROM API"+e);
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("WEATHER_DATA_Forecast",error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public ImageView setWeatherImageView(String weatherIconId) {
//        https://www.flaticon.com/packs/weather-400?word=weather%20forecast

        return weatherImageView;
    }
}