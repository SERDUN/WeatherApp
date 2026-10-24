package com.example.dmitro.weatherapp.service.weather_service.weather_service;

import com.example.dmitro.weatherapp.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url().newBuilder()
                .addQueryParameter("APPID", BuildConfig.WEATHER_API_KEY)
                .build();
        return chain.proceed(request.newBuilder().url(url).build());
    }
}
