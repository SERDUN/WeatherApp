package com.example.dmitro.weatherapp;

import android.app.Application;

import com.google.android.libraries.places.api.Places;

public class WeatherApp extends Application {

    private static WeatherApp instance;

    public static WeatherApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        if (!BuildConfig.PLACES_API_KEY.isEmpty() && !Places.isInitialized()) {
            Places.initializeWithNewPlacesApiEnabled(this, BuildConfig.PLACES_API_KEY);
        }
    }
}
