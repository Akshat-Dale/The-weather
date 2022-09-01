package com.akshatdale.theweather;

public class SetForecastWeatherData {
     String temperature;
     String iconId;
     String weatherDescription;

    public SetForecastWeatherData(String temperature, String iconId, String weatherDescription) {
        this.temperature = temperature;
        this.iconId = iconId;
        this.weatherDescription = weatherDescription;
    }
}
