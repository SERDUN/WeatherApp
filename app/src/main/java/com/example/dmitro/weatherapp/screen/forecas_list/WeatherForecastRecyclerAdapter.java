package com.example.dmitro.weatherapp.screen.forecas_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.data.model.geo.places.Prediction;
import com.example.dmitro.weatherapp.data.model.weather.WeatherResponse;
import com.example.dmitro.weatherapp.screen.RecyclerViewEmptySupport;
import com.example.dmitro.weatherapp.screen.select_city.list.RecyclerListener;

import java.util.ArrayList;

/**
 * Created by dmitro on 27.09.17.
 */

public class WeatherForecastRecyclerAdapter extends RecyclerViewEmptySupport.Adapter<WeatherForecastRecyclerAdapter.PlaceHolder> {
    private ArrayList<WeatherResponse> weather;
    private int lastCheckedPosition = -1;


    public int getLastCheckedPosition() {
        return lastCheckedPosition;
    }

    public WeatherForecastRecyclerAdapter(ArrayList<WeatherResponse> weather) {
        this.weather = weather;
    }

    public void updateData(ArrayList<WeatherResponse> weather) {
        this.weather = weather;
        notifyDataSetChanged();

    }

    @Override
    public WeatherForecastRecyclerAdapter.PlaceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.forecast_weather_item, parent, false);
        return new WeatherForecastRecyclerAdapter.PlaceHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherForecastRecyclerAdapter.PlaceHolder holder, int position) {
        holder.bindView(weather.get(position));


    }

    @Override
    public int getItemCount() {
        return weather.size();
    }

    class PlaceHolder extends RecyclerView.ViewHolder {
        TextView tvCountry;
        TextView tvCity;

        public PlaceHolder(View itemView) {
            super(itemView);
//            tvCountry = (TextView) itemView.findViewById(R.id.tvCountry);
//            tvCity = (TextView) itemView.findViewById(R.id.tvCity);

//            itemView.setOnClickListener(v -> {
//                lastCheckedPosition = getAdapterPosition();
//                notifyItemRangeChanged(0, weather.size());
//                notifyItemChanged(lastCheckedPosition);
//                recyclerViewListener.onClick(weather.get(getAdapterPosition()));
//            });
        }

        public void bindView(WeatherResponse p) {
//            tvCountry.setText(p.getTerms().get(p.getTerms().size() - 1).getValue());
//            tvCity.setText(p.getDescription());


        }
    }
}
