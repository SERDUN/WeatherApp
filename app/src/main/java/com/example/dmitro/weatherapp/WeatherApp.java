package com.example.dmitro.weatherapp;

import android.app.Application;
import android.content.Context;


import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by dmitro on 26.09.17.
 */

public class WeatherApp extends Application {

    private static WeatherApp instance;

    public static WeatherApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Realm.init(this);

        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/calibri.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
        instance = this;


    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }
}
