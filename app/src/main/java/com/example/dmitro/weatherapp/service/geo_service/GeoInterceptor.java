package com.example.dmitro.weatherapp.service.geo_service;


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


public class GeoInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request.url().newBuilder().addQueryParameter("key",  WeatherApp.getInstance().getApplicationContext().getString(R.string.google_map_key))
                .addQueryParameter("format", "json").build();
        request = request.newBuilder().url(url).build();
        Log.d("test_iterapt", "intercept: "+request.url());
        return chain.proceed(request);
    }
}
