package com.example.dmitro.weatherapp.service.geo_service;

import com.example.dmitro.weatherapp.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class GeoInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url().newBuilder()
                .addQueryParameter("key", BuildConfig.PLACES_API_KEY)
                .addQueryParameter("format", "json")
                .build();
        return chain.proceed(request.newBuilder().url(url).build());
    }
}
