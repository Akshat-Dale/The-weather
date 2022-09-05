package com.akshatdale.theweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final DecimalFormat df = new DecimalFormat("0.0");
    public static final int REQUEST_CODE = 100;
    EditText cityEditText;
    TextView cityTextView, weatherConditionTextView, temperatureTextView, textViewSunriseTime, textViewSunsetTime, textViewWindSpeed, textViewHumidityPercentage;
    ImageView weatherImageView;
    LinearLayout linearLayoutFirst;
    LinearLayoutManager layoutManagerForRecycleView;
    RecyclerView recyclerViewForecast;
    String  currentCity, currentTemperature, currentWindSpeed, currentWeatherDescriptionMain, currentHumidity,
            sunriseToday, sunsetToday;
    ArrayList<SetForecastWeatherData> setForecastWeatherDataArrayList;
    SwipeRefreshLayout swipeRefresh;
    int currentIconId,forecastIconId;

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
        swipeRefresh = findViewById(R.id.swipeRefresh);
//
//        SETTING HORIZONTAL LIST LAYOUT
        layoutManagerForRecycleView = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
//        SETTING LAYOUT IN RECYCLER VIEW
        recyclerViewForecast.setLayoutManager(layoutManagerForRecycleView);
        recyclerViewForecast.setItemAnimator(new DefaultItemAnimator());
        setForecastWeatherDataArrayList = new ArrayList<>();
//        CHECK INTERNET CONNECTIVITY
//        checkInternet();

//        SET WEATHER BACKGROUND
        setWeatherBackground();
//        GETTING CURRENT LOCATION
        String currentLocation = getLocation();
        Log.i("WEATHER_DATA_location",currentLocation);
//        CALLING API TO GET CURRENT AND FORECAST DATA
       getWeatherDataCallAPI(currentLocation);
       getForecastWeatherData(currentLocation);


//       WHILE SWIPE FROM TOP LOCATION REFRESH getLocation() called
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                String currentLocation = getLocation();
                Log.i("WEATHER_DATA_location",currentLocation);
//        CALLING API TO GET CURRENT AND FORECAST DATA
                getWeatherDataCallAPI(currentLocation);
                getForecastWeatherData(currentLocation);

                swipeRefresh.setColorSchemeColors(Color.RED);
                  swipeRefresh.setSoundEffectsEnabled(true);
                swipeRefresh.setRefreshing(false);
            }
        });

    }
//
////    CHECKING INTERNET CONNECTION
//public void checkInternet(){
//        ConnectivityManager connectivityManager = (ConnectivityManager)  getSystemService(CONNECTIVITY_SERVICE);
//        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()==NetworkInfo.State.DISCONNECTED
//                ||connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()==NetworkInfo.State.DISCONNECTED){
//            Toast.makeText(getApplicationContext(), "Check internet connection", Toast.LENGTH_SHORT).show();
//
//        }
//        else{
//            Toast.makeText(getApplicationContext(), "Off", Toast.LENGTH_SHORT).show();
//        }



    //    GETTING DATA CURRENT WEATHER DATA FROM API
    public void getWeatherDataCallAPI(String cityName) {

//        GETTING WEATHER DATA THROW LAT LONG
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(MainActivity.this);

        String getWeatherURL = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=b63f3db52cd36ca5cc60f382ff723087&units=metric";
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
                    textViewSunriseTime.setText(istTimeConverter(sunriseToday));
                    textViewSunsetTime.setText(istTimeConverter(sunsetToday));
//                    GETTING FROM ARRAY "weather" TO OBJECT AND GET STRING FROM OBJECT AND SET SETTEXT
                    JSONArray jsonArray = response.getJSONArray("weather");
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    currentWeatherDescriptionMain = jsonObject.getString("main");
                     currentIconId = jsonObject.getInt("id");
                     weatherImageView.setImageResource(new SetWeatherIcon().setIconCurrent(currentIconId));
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

    }



    public void getForecastWeatherData(String cityName){

        String forecastWeatherUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + cityName+ "&appid=b63f3db52cd36ca5cc60f382ff723087&units=metric";
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
                        String dateTime = jsonObject.getString("dt");
                        String temperature = jsonObject.getJSONObject("main").getString("temp");
                        double temperatureDouble = Double.parseDouble(temperature);
                        String temperatureDigit = df.format(temperatureDouble);
//                        Log.i("WEATHER_DATA_Forecast", "Date " + dateTime + " Temp. " + temperatureDigit);

                        JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                        JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                        String descriptionMain = jsonObjectWeather.getString("main");
                        String description = jsonObjectWeather.getString("description");
                        String icon = jsonObjectWeather.getString("icon");
                         int forecastIconId = jsonObjectWeather.getInt("id");
//                        SETTING WEATHER ICON

                        String dateIst = istDateConverter(dateTime);
                        String timeIst = istTimeConverter(dateTime);

//                        SETTING DATA ON REECYCLER VIEW AND SEND ICON DETAIL TO SetWeatherIcon CLASS WHICH RETURN RIGHT ICON ID FOR SET
                        setForecastWeatherDataArrayList.add(new SetForecastWeatherData(timeIst,dateIst,temperature,new SetWeatherIcon().setForecastIcon(forecastIconId,dateTime)));

                        ForecastRecycleAdapter forecastRecycleAdapter = new ForecastRecycleAdapter(MainActivity.this,setForecastWeatherDataArrayList);
                        recyclerViewForecast.setAdapter(forecastRecycleAdapter);
                        Log.i("WEATHER_DATA_Forecast", "Description " + descriptionMain + " Icon. " + icon  + " IconId. "+forecastIconId+" Descrition  " +description);
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


    //    CONVERT UNIX TIME FORMAT TO IST TIME FORMAT
    public String istTimeConverter(String seconds) {
        // Unix seconds
        long unix_seconds = Long.parseLong(seconds);
        // convert seconds to milliseconds
        Date date = new Date(unix_seconds * 1000L);
        // format of the date
        @SuppressLint("SimpleDateFormat") SimpleDateFormat jdf = new SimpleDateFormat("HH:mm");
        String time = jdf.format(date);
        System.out.println("\n" + time + "\n");

//        IT RETURN TIME
        return time;
    }

    //    CONVERT UNIX TIME FORMAT TO IST TIME FORMAT
    public String istDateConverter(String seconds) {
        // Unix seconds
        long unix_seconds = Long.parseLong(seconds);
        // convert seconds to milliseconds
        Date date = new Date(unix_seconds * 1000L);
        // format of the date
        @SuppressLint("SimpleDateFormat") SimpleDateFormat jdf = new SimpleDateFormat("dd/MM");
        String dateOnly = jdf.format(date);
        System.out.println("\n" + dateOnly + "\n");
//        IT RETURN DATE
        return dateOnly;
    }



//    GETTING TEXT FROM EDITTEXT THAT GIVE USER
    public void userEnterCity(View view){
        String city = cityEditText.getText().toString();
        if (city.isEmpty()){
            Toast.makeText(getApplicationContext(), "Enter city name", Toast.LENGTH_SHORT).show();
        }
        else{
//            REMOVE CITY FORECAST VIEW, THAT ARE STORED FROM LOCATION
            setForecastWeatherDataArrayList.clear();
            getWeatherDataCallAPI(city);
            getForecastWeatherData(city);
        }
    }




//    GETTING USER LOCTION BY GPS
    public String getLocation(){

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        CHEKING PERMISSION FOR LOCATION
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
         && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

//            REQUEST FOR PERMISSION WITH DAILOG BOX
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION
                    , Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
        }


            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            String cityName = "Not Found";
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> addressList = geocoder.getFromLocation(latitude,longitude,10);
                for (Address address:addressList) {
                    if (address != null) {
                        String city = address.getLocality();
                        Log.i("WEATHER_DATA_location", "Location is " + city);
                        if (city != null && !city.equals("")) {
                            cityName = city;
                        } else {
                            Toast.makeText(getApplicationContext(), "City Not Found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        Log.i("WEATHER_DATA_location","Location is "+cityName);
        return cityName;
          }




//CHECKING LOCATION PERMISSION RESULT
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }



    //SET WEATHER ICON ON WEATHER IMAGE VIEW
    public void setWeatherBackground() {

        int time =0;
        SimpleDateFormat formatter = new SimpleDateFormat("HH");
        Date date = new Date();
         time = Integer.parseInt(formatter.format(date));
        Log.i("WEATHER_DATA_Time", String.valueOf(time));


            try {


//        DAY BACKGROUND
                if (time >= 6 && time < 18) {
                    linearLayoutFirst.setBackgroundResource(R.drawable.day_background);
                }
//        NIGHT BACKGROUND
                else {
                    linearLayoutFirst.setBackgroundResource(R.drawable.night_background);
                }

            }
            catch (Exception e) {
                Log.i("WEATHER_DATA_iconSet", e.toString());
            }


    }
}