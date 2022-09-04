package com.akshatdale.theweather;

import android.widget.ImageView;

public class SetForecastWeatherData {
     String time;
     String date;
     String temperature;
     int weatherIcon;



    public SetForecastWeatherData(String time,String date, String temperature,int weatherIcon) {
        this.time = time;
        this.date = date;
        this.temperature = temperature;
        this.weatherIcon = weatherIcon;


    }
}
