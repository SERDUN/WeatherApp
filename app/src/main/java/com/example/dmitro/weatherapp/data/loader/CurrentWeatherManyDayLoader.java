package com.example.dmitro.weatherapp.data.loader;

import android.content.Context;

import com.example.dmitro.weatherapp.data.repository.WeatherRepositoryManager;
import com.example.dmitro.weatherapp.data.loader.base.BaseLoader;
import com.example.dmitro.weatherapp.data.loader.base.RequestResult;
import com.example.dmitro.weatherapp.data.loader.base.Response;
import com.example.dmitro.weatherapp.utils.Injection;

import java.io.IOException;

/**
 * Created by dmitro on 01.10.17.
 */

public class CurrentWeatherManyDayLoader extends BaseLoader {
    public CurrentWeatherManyDayLoader(Context context) {
        super(context);
    }

    @Override
    protected Response apiCall() throws IOException {
        WeatherRepositoryManager manager = Injection.provideManager();
        return new Response().setRequestResult(RequestResult.SUCCESS).setAnswer(manager.getCacheManyDayWeather());
    }
}
