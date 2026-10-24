package com.example.dmitro.weatherapp.screen.weather.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.data.model.weather.current.WeatherResponse;
import com.example.dmitro.weatherapp.screen.select_city.list.RecyclerListener;
import com.example.dmitro.weatherapp.utils.ImageLoader;
import com.example.dmitro.weatherapp.utils.MyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WeatherRecyclerAdapter extends RecyclerView.Adapter<WeatherRecyclerAdapter.PlaceHolder> {
    private ArrayList<HashMap<String, ArrayList<WeatherResponse>>> weatherResponses;
    private final RecyclerListener recyclerViewListener;

    public WeatherRecyclerAdapter(ArrayList<HashMap<String, ArrayList<WeatherResponse>>> weatherResponses, RecyclerListener listener) {
        this.recyclerViewListener = listener;
        this.weatherResponses = weatherResponses;
    }

    @SuppressWarnings("NotifyDataSetChanged")
    public void updateData(ArrayList<HashMap<String, ArrayList<WeatherResponse>>> weather) {
        weatherResponses = weather;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_weather, parent, false);
        return new PlaceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceHolder holder, int position) {
        holder.bindView(weatherResponses.get(position));
    }

    @Override
    public int getItemCount() {
        return weatherResponses.size();
    }

    class PlaceHolder extends RecyclerView.ViewHolder {
        final TextView dayTextView;
        final TextView tempTextView;
        final ImageView iconImageView;

        PlaceHolder(View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.day_tv);
            tempTextView = itemView.findViewById(R.id.temp_item_tv);
            iconImageView = itemView.findViewById(R.id.weather_icon_for_item_iv);
            itemView.setOnClickListener(view -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    recyclerViewListener.onClick(weatherResponses.get(position));
                }
            });
        }

        void bindView(HashMap<String, ArrayList<WeatherResponse>> weatherForDay) {
            for (Map.Entry<String, ArrayList<WeatherResponse>> entry : weatherForDay.entrySet()) {
                ArrayList<WeatherResponse> items = entry.getValue();
                if (items == null || items.isEmpty()) {
                    continue;
                }
                dayTextView.setText(entry.getKey());
                ImageLoader.loadWeatherIcon(iconImageView, items.get(0).getWeather().get(0).getIcon());
                tempTextView.setText(getMinAndMaxTemp(items));
            }
        }

        private String getMinAndMaxTemp(ArrayList<WeatherResponse> weatherResponses) {
            double maxT = weatherResponses.get(0).getMain().getTempMax();
            double minT = weatherResponses.get(0).getMain().getTempMin();
            for (WeatherResponse weatherResponse : weatherResponses) {
                minT = Math.min(minT, weatherResponse.getMain().getTempMin());
                maxT = Math.max(maxT, weatherResponse.getMain().getTempMax());
            }
            return MyUtil.createTemperatureDiapasonString(maxT, minT);
        }
    }
}
