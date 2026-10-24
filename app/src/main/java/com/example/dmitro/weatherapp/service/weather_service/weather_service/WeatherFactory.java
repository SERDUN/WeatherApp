package com.example.dmitro.weatherapp.service.weather_service.weather_service;

import com.example.dmitro.weatherapp.BuildConfig;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherFactory {
    private static volatile WeatherApiServer service;

    public static WeatherApiServer getService() {
        WeatherApiServer currentService = service;
        if (currentService == null) {
            synchronized (WeatherFactory.class) {
                currentService = service;
                if (currentService == null) {
                    currentService = service = buildRetrofit().create(WeatherApiServer.class);
                }
            }
        }
        return currentService;
    }

    private static Retrofit buildRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new WeatherInterceptor())
                .build();
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_WEATHER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }
}
