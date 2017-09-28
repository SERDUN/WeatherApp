package com.example.dmitro.weatherapp.network.geoService;


import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.WeatherApp;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dmitro on 25.09.17.
 */

public class GeoFactory {
    private static GeoService service;
    private static OkHttpClient okHttpClient;


    public static GeoService getService() {
        GeoService currentService = service;
        if (currentService == null) {
            synchronized (GeoFactory.class) {
                if (currentService == null) {
                    currentService = service = getRetrofitBuilder().create(GeoService.class);
                }
            }
        }
        return currentService;
    }


    private static Retrofit getRetrofitBuilder() {
        return new Retrofit.Builder().client(getOkHttpClient())
                .baseUrl(WeatherApp.getInstance().getApplicationContext().getString(R.string.BASE_GEO_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static OkHttpClient getOkHttpClient() {
        OkHttpClient client = okHttpClient;
        if (client == null) {
            synchronized (GeoFactory.class) {
                client = okHttpClient;
                if (client == null) {
                    client = okHttpClient = buildOkHttpClient();
                }
            }
        }
        return client;
    }

    private static OkHttpClient buildOkHttpClient() {
        return new OkHttpClient.Builder().addInterceptor(new GeoInterceptor()).build();
    }
}
