package com.akshatdale.theweather;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ForecastRecycleAdapter extends RecyclerView.Adapter<ForecastRecycleAdapter.ViewHolder> {

    Context context;
    ArrayList<SetForecastWeatherData> forecastWeatherDataArrayList;

      ForecastRecycleAdapter(Context context, ArrayList<SetForecastWeatherData> weatherDataArrayList) {
        this.context = context;
        this.forecastWeatherDataArrayList = weatherDataArrayList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
          TextView time,date,temperature;
          ImageView weatherImage;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            time = itemView.findViewById(R.id.textviewForescastTime);
            date = itemView.findViewById(R.id.textviewForescastDate);
            temperature = itemView.findViewById(R.id.textviewForescastTemperature);
            weatherImage = itemView.findViewById(R.id.imageViewForecast);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.forcast_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
MainActivity mainActivity = new MainActivity();
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

          holder.time.setText(forecastWeatherDataArrayList.get(position).time);
          holder.date.setText(forecastWeatherDataArrayList.get(position).date);
          holder.temperature.setText(forecastWeatherDataArrayList.get(position).temperature+"°C");
          holder.weatherImage.setImageResource(forecastWeatherDataArrayList.get(position).weatherIcon);


    }

    @Override
    public int getItemCount() {
        return forecastWeatherDataArrayList.size();
    }


}
