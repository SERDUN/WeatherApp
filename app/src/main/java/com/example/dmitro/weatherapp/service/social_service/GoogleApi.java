package com.example.dmitro.weatherapp.service.social_service;

import android.content.Context;
import android.content.Intent;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.WeatherApp;
import com.example.dmitro.weatherapp.data.repository.WeatherRepositoryManager;
import com.example.dmitro.weatherapp.utils.Injection;
import com.google.android.gms.plus.PlusShare;

import static com.example.dmitro.weatherapp.screen.weather.fragment.WeatherCurrentDetailsFragment.POSTFIX_BY_ICON_URL;
import static com.example.dmitro.weatherapp.service.social_service.FacebookApi.LOCATION;
import static com.example.dmitro.weatherapp.service.social_service.FacebookApi.TEMPERATURE;

/**
 * Created by dmitro on 04.10.17.
 */

public class GoogleApi {
    public static Intent shareWeather(Context context) {
        WeatherRepositoryManager serviceManager = Injection.provideManager();
        String url = WeatherApp.getInstance().getBaseContext().getString(R.string.icon_weather_url) + serviceManager.getCacheWeather().getWeather().get(0).getIcon() + POSTFIX_BY_ICON_URL;
        String caption = TEMPERATURE + serviceManager.getCacheWeather().getMain().getTemp() + "  " + LOCATION + serviceManager.getCacheWeather().getName();

        Intent shareIntent = new PlusShare.Builder(context)
                .setType("text/plain")
                .setText(caption)
                .getIntent();
        return shareIntent;

    }
}
