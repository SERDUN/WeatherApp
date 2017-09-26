package com.example.dmitro.weatherapp.network.weatherService;

import com.example.dmitro.weatherapp.BuildConfig;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dmitro on 25.09.17.
 */

public class WeatherFactory {
    private static WeatherApiServer service;
    private static OkHttpClient okHttpClient;


    public static WeatherApiServer getService() {
        WeatherApiServer currentService = service;
        if (currentService == null) {
            synchronized (WeatherApiServer.class) {
                if (currentService == null) {
                    currentService = service = getRetrofitBuilder().create(WeatherApiServer.class);
                }
            }
        }
        return currentService;
    }


    private static Retrofit getRetrofitBuilder() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_WEATHER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient());


        return builder.build();

    }

    private static OkHttpClient getOkHttpClient() {
        OkHttpClient client = okHttpClient;
        if (client == null) {
            synchronized (WeatherApiServer.class) {
                client = okHttpClient;
                if (client == null) {
                    client = okHttpClient = buildOkHttpClient();
                }
            }
        }
        return client;
    }

    private static OkHttpClient buildOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(new WeatherInterceptor()).build();
    }


}
