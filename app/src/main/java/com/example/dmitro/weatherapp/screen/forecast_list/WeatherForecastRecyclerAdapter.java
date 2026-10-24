package com.example.dmitro.weatherapp.screen.forecast_list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dmitro.weatherapp.data.model.weather.current.WeatherResponse;
import com.example.dmitro.weatherapp.databinding.ForecastWeatherItemBinding;
import com.example.dmitro.weatherapp.screen.weather.fragment.WeatherDetailsBinder;

import java.util.ArrayList;

public class WeatherForecastRecyclerAdapter extends RecyclerView.Adapter<WeatherForecastRecyclerAdapter.PlaceHolder> {
    private ArrayList<WeatherResponse> weather;

    public WeatherForecastRecyclerAdapter(ArrayList<WeatherResponse> weather) {
        this.weather = weather;
    }

    @SuppressWarnings("NotifyDataSetChanged")
    public void updateData(ArrayList<WeatherResponse> weather) {
        this.weather = weather;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ForecastWeatherItemBinding binding = ForecastWeatherItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PlaceHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceHolder holder, int position) {
        holder.bindView(weather.get(position));
    }

    @Override
    public int getItemCount() {
        return weather.size();
    }

    static class PlaceHolder extends RecyclerView.ViewHolder {
        private final ForecastWeatherItemBinding binding;

        PlaceHolder(ForecastWeatherItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindView(WeatherResponse wr) {
            if (wr == null) {
                return;
            }
            binding.timeTv.setText(wr.getDate());
            WeatherDetailsBinder.bind(binding.details, wr);
        }
    }
}
