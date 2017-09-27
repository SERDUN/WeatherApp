package com.example.dmitro.weatherapp.network.weatherService;

import com.example.dmitro.weatherapp.data.model.weather.WeatherResponse;
import com.example.dmitro.weatherapp.data.model.weather.many_day.ResponseManyDayWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by dmitro on 25.09.17.
 */

public interface WeatherApiServer {

    @GET("/data/2.5/weather?units=metric")
    public Call<WeatherResponse> getCurrentWeather(@Query("lat") double lat,@Query("lon") double lon);

    @GET("/data/2.5/forecast?units=metric")
    public Call<ResponseManyDayWeather> getWeatherForManyDay(@Query("lat") double lat, @Query("lon") double lon);


}
