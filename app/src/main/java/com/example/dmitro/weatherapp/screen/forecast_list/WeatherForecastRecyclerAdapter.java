package com.example.dmitro.weatherapp.screen.forecast_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.WeatherApp;
import com.example.dmitro.weatherapp.data.model.weather.current.WeatherResponse;
import com.example.dmitro.weatherapp.screen.RecyclerViewEmptySupport;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dmitro on 27.09.17.
 */

public class WeatherForecastRecyclerAdapter extends RecyclerViewEmptySupport.Adapter<WeatherForecastRecyclerAdapter.PlaceHolder> {
    private ArrayList<WeatherResponse> weather;
    private int lastCheckedPosition = -1;
    private Context context;

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
        WeatherForecastRecyclerAdapter.PlaceHolder placeHolder = new WeatherForecastRecyclerAdapter.PlaceHolder(view);
        context = parent.getContext();
        return placeHolder;
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
        @BindView(R.id.temperature_tv)
        public TextView temperatureTextView;

        @BindView(R.id.wind_tv)
        public TextView windTextView;

        @BindView(R.id.diapason_temp_tv)
        public TextView diapasonTempTextView;

        @BindView(R.id.humidity_tv)
        public TextView humidityTextView;

        @BindView(R.id.time_tv)
        public TextView dateTextView;

        @BindView(R.id.weather_icon_iv)
        public ImageView weatherIconImageView;

        public PlaceHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(WeatherResponse p) {
            initFields(p);


        }

        public static final String FRAGMENT_KEY = "WeatherCurrentDetailsFragment";
        public static final String POSTFIX_BY_ICON_URL = ".png";
        public static final String POSTFIX_BY_TEMP_CELSIUS = "°";
        public static final String POSTFIX_BY_TEMP_PERCENT = " %";
        public static final String POSTFIX_BY_TEMP_MS = " m/s";

        private void initFields(WeatherResponse wr) {
            if (wr != null) {
                dateTextView.setText(wr.getDate());
                temperatureTextView.setText(wr.getMain().getTemp().toString() + POSTFIX_BY_TEMP_CELSIUS);
                humidityTextView.setText(wr.getMain().getHumidity().toString() + POSTFIX_BY_TEMP_PERCENT);
                windTextView.setText(wr.getWind().getSpeed().toString() + POSTFIX_BY_TEMP_MS);
                diapasonTempTextView.setText(wr.getMain().getTempMax() + POSTFIX_BY_TEMP_CELSIUS + " - " + wr.getMain().getTempMin() + POSTFIX_BY_TEMP_CELSIUS);
                String url = WeatherApp.getInstance().getBaseContext().getString(R.string.ICON_WEATHER_URL) + wr.getWeather().get(0).getIcon() + POSTFIX_BY_ICON_URL;
                Picasso.with(context).load(url).into(weatherIconImageView);

            }
        }
    }
}
