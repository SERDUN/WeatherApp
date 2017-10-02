package com.example.dmitro.weatherapp.service.weather_service.weather_loader.realization_loader;

import android.content.Context;

import com.example.dmitro.weatherapp.data.repository.WeatherRepositoryManager;
import com.example.dmitro.weatherapp.service.weather_service.weather_loader.base.BaseLoader;
import com.example.dmitro.weatherapp.service.weather_service.weather_loader.base.RequestResult;
import com.example.dmitro.weatherapp.service.weather_service.weather_loader.base.Response;
import com.example.dmitro.weatherapp.utils.Injection;

import java.io.IOException;

/**
 * Created by dmitro on 30.09.17.
 */

public class CurrentWeatherLoader extends BaseLoader {



    public CurrentWeatherLoader(Context context) {
        super(context);

    }


    @Override
    protected Response apiCall() throws IOException {
        WeatherRepositoryManager manager = Injection.provideManager();
        return new Response().setRequestResult(RequestResult.SUCCESS).setAnswer(manager.getCacheWeather());
    }
}
