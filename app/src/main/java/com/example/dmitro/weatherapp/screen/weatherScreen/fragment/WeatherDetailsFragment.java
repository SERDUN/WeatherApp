package com.example.dmitro.weatherapp.screen.weatherScreen.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.data.model.weather.WeatherResponse;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherDetailsFragment extends Fragment {


    public static final String FRAGMENT_KEY = "WeatherDetailsFragment";


    @BindView(R.id.city_name_tv)
    public TextView cityNameTextView;

    @BindView(R.id.temperature_tv)
    public TextView temperatureTextView;

    @BindView(R.id.wind_tv)
    public TextView windTextView;

    @BindView(R.id.pressure_tv)
    public TextView pressureTextView;

    @BindView(R.id.humidity_tv)
    public TextView humidityTextView;


    public WeatherDetailsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static WeatherDetailsFragment newInstance(String param1, String param2) {
        WeatherDetailsFragment fragment = new WeatherDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initFields(WeatherResponse wr) {
        if (wr != null) {
            Log.d(FRAGMENT_KEY, "initFields: " + cityNameTextView);
            cityNameTextView.setText(wr.getName());
            temperatureTextView.setText(wr.getMain().getTemp().toString());
            windTextView.setText(wr.getWind().getSpeed().toString());
            pressureTextView.setText(wr.getMain().getPressure().toString());
            humidityTextView.setText(wr.getMain().getHumidity().toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather_details, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            WeatherResponse wr = (WeatherResponse) getArguments().getSerializable(FRAGMENT_KEY);
            initFields(wr);

        }
        return view;
    }


}
