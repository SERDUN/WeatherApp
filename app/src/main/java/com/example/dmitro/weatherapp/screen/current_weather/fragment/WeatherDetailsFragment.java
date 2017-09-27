package com.example.dmitro.weatherapp.screen.current_weather.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.WeatherApp;
import com.example.dmitro.weatherapp.data.model.weather.WeatherResponse;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherDetailsFragment extends Fragment {

    public static final String FRAGMENT_KEY = "WeatherDetailsFragment";
    public static final String POSTFIX_BY_ICON_URL = ".png";
    public static final String POSTFIX_BY_TEMP_CELSIUS = "°";
    public static final String POSTFIX_BY_TEMP_PERCENT = " %";
    public static final String POSTFIX_BY_TEMP_MS = " m/s";


    @BindView(R.id.temperature_tv)
    public TextView temperatureTextView;

    @BindView(R.id.wind_tv)
    public TextView windTextView;

    @BindView(R.id.diapason_temp_tv)
    public TextView diapasonTempTextView;

    @BindView(R.id.humidity_tv)
    public TextView humidityTextView;

    @BindView(R.id.weather_icon_iv)
    public ImageView weatherIconImageView;


    public WeatherDetailsFragment() {
    }

    public static WeatherDetailsFragment newInstance(String param1, String param2) {
        WeatherDetailsFragment fragment = new WeatherDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void notifyField() {
        if (getArguments() != null) {
            WeatherResponse wr = (WeatherResponse) getArguments().getSerializable(FRAGMENT_KEY);
            initFields(wr);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_details, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            WeatherResponse wr = (WeatherResponse) getArguments().getSerializable(FRAGMENT_KEY);
            initFields(wr);

        }
        return view;
    }


    //// TODO: 26.09.17 what be
    private void initFields(WeatherResponse wr) {
        if (wr != null) {
            temperatureTextView.setText(wr.getMain().getTemp().toString() + POSTFIX_BY_TEMP_CELSIUS);
            humidityTextView.setText(wr.getMain().getHumidity().toString() + POSTFIX_BY_TEMP_PERCENT);
            windTextView.setText(wr.getWind().getSpeed().toString() + POSTFIX_BY_TEMP_MS);
            diapasonTempTextView.setText(wr.getMain().getTempMax() + POSTFIX_BY_TEMP_CELSIUS + " - " + wr.getMain().getTempMin() + POSTFIX_BY_TEMP_CELSIUS);
            String url = WeatherApp.getInstance().getBaseContext().getString(R.string.ICON_WEATHER_URL) + wr.getWeather().get(0).getIcon() + POSTFIX_BY_ICON_URL;
            Picasso.with(getContext()).load(url).into(weatherIconImageView);

        }
    }


}
