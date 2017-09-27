package com.example.dmitro.weatherapp.data.repository;

import com.example.dmitro.weatherapp.data.model.geo.placeDetails.PlaceDetails;
import com.example.dmitro.weatherapp.data.model.geo.places.Places;
import com.example.dmitro.weatherapp.data.model.weather.WeatherResponse;
import com.example.dmitro.weatherapp.data.model.weather.many_day.ResponseManyDayWeather;
import com.example.dmitro.weatherapp.utils.callback.Action0;
import com.example.dmitro.weatherapp.utils.callback.Action1;

/**
 * Created by dmitro on 25.09.17.
 */

public interface WeatherDataSource {

    public void getCurrentWeather(double lat, double lon, Action1<WeatherResponse> success, Action1<Throwable> failure, Action0 complete);

    public void getPlaceByName(String name, Action1<Places> success, Action1<Throwable> failure, Action0 complete);

    public void getWeatherForFiveDay(double lat, double lon, Action1<ResponseManyDayWeather> success, Action1<Throwable> failure, Action0 complete);


    public void getPlaceId(String placeId, Action1<PlaceDetails> success, Action1<Throwable> failure, Action0 complete);

}
