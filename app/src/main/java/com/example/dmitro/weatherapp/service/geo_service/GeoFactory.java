package com.example.dmitro.weatherapp.service.geo_service;

import com.example.dmitro.weatherapp.BuildConfig;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeoFactory {
    private static volatile GeoService service;

    public static GeoService getService() {
        GeoService currentService = service;
        if (currentService == null) {
            synchronized (GeoFactory.class) {
                currentService = service;
                if (currentService == null) {
                    currentService = service = buildRetrofit().create(GeoService.class);
                }
            }
        }
        return currentService;
    }

    private static Retrofit buildRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new GeoInterceptor())
                .build();
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_GEO_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }
}
