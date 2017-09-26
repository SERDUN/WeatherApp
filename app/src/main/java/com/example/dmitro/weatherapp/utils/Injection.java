package com.example.dmitro.weatherapp.utils;

import com.example.dmitro.weatherapp.data.repository.WeatherRepositoryManager;
import com.example.dmitro.weatherapp.data.repository.local.WeatherLocalRepository;
import com.example.dmitro.weatherapp.data.repository.remote.WeatherRemoteRepository;

/**
 * Created by dmitro on 25.09.17.
 */

public class Injection {
    public static WeatherRepositoryManager provideManager() {
        return (WeatherRepositoryManager) WeatherRepositoryManager.getInstance(new WeatherLocalRepository(),
                new WeatherRemoteRepository());
    }
}
