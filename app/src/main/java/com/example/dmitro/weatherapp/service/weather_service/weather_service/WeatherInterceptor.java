package com.example.dmitro.weatherapp.service.weather_service.weather_service;

import android.util.Log;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.WeatherApp;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by dmitro on 25.09.17.
 */

public class WeatherInterceptor implements Interceptor {
    public final String LOG_WEATHER_INTERCEPTOR ="WeatherInterceptor";
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url().newBuilder().addQueryParameter("APPID", WeatherApp.getInstance().getApplicationContext().getString(R.string.weather_api_key)).build();
        Log.d(LOG_WEATHER_INTERCEPTOR, "intercept: " + url.toString());
        request = request.newBuilder().url(url).build();
        return chain.proceed(request);
    }
}
