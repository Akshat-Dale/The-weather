package com.akshatdale.theweather;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ForecastRecycleAdapter extends RecyclerView.Adapter<ForecastRecycleAdapter.ViewHolder> {

    Context context;
    ArrayList<SetForecastWeatherData> weatherDataArrayList;
    public ForecastRecycleAdapter(Context context, ArrayList<SetForecastWeatherData> weatherDataArrayList) {
        this.context = context;
        this.weatherDataArrayList = weatherDataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return weatherDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
