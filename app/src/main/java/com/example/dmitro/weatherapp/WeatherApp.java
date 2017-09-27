package com.example.dmitro.weatherapp;

import android.app.Application;

/**
 * Created by dmitro on 26.09.17.
 */

public class WeatherApp extends Application {

    private static WeatherApp instance;

    public static  WeatherApp getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }
}
