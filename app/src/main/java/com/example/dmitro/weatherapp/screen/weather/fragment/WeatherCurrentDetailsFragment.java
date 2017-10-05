package com.example.dmitro.weatherapp.screen.weather.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.WeatherApp;
import com.example.dmitro.weatherapp.data.loader.CurrentWeatherLoader;
import com.example.dmitro.weatherapp.data.loader.base.Response;
import com.example.dmitro.weatherapp.data.model.weather.current.WeatherResponse;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherCurrentDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Response> {

    public static final String FRAGMENT_KEY = "WeatherCurrentDetailsFragment";
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

    private Loader<Response> loader;


    public WeatherCurrentDetailsFragment() {
    }

    public static WeatherCurrentDetailsFragment newInstance() {
        return new WeatherCurrentDetailsFragment();


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loader = getLoaderManager().initLoader(FRAGMENT_KEY.hashCode(), savedInstanceState, this);

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
            String url = WeatherApp.getInstance().getBaseContext().getString(R.string.icon_weather_url) + wr.getWeather().get(0).getIcon() + POSTFIX_BY_ICON_URL;
            Picasso.with(getContext()).load(url).into(weatherIconImageView);

        }
    }


    @Override
    public Loader<Response> onCreateLoader(int id, Bundle args) {
        loader= new CurrentWeatherLoader(getContext());
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Response> loader, Response data) {
        if (data.getTypedAnswer() != null) {
            if (getArguments() == null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(FRAGMENT_KEY, data.getTypedAnswer());

            } else {
                getArguments().putSerializable(FRAGMENT_KEY, data.getTypedAnswer());
            }


            initFields(data.getTypedAnswer());
        }
    }

    @Override
    public void onLoaderReset(Loader<Response> loader) {

    }
}
