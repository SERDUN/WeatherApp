package com.example.dmitro.weatherapp.data.repository;

import com.example.dmitro.weatherapp.data.model.geo.placeDetails.PlaceDetails;
import com.example.dmitro.weatherapp.data.model.geo.places.Places;
import com.example.dmitro.weatherapp.data.model.weather.WeatherResponse;
import com.example.dmitro.weatherapp.data.model.weather.many_day.ResponseManyDayWeather;
import com.example.dmitro.weatherapp.data.repository.local.WeatherLocalRepository;
import com.example.dmitro.weatherapp.data.repository.remote.WeatherRemoteRepository;
import com.example.dmitro.weatherapp.utils.callback.Action0;
import com.example.dmitro.weatherapp.utils.callback.Action1;

/**
 * Created by dmitro on 25.09.17.
 */

public class WeatherRepositoryManager implements WeatherDataSource {
    private static WeatherDataSource INSTANCE = null;
    private final WeatherLocalRepository weatherLocalRepository;
    private final WeatherRemoteRepository weatherRemoteRepository;

    private WeatherRepositoryManager(WeatherLocalRepository weatherLocalRepository, WeatherRemoteRepository weatherRemoteRepository) {
        this.weatherLocalRepository = weatherLocalRepository;
        this.weatherRemoteRepository = weatherRemoteRepository;
    }

    public static WeatherDataSource getInstance(WeatherLocalRepository local, WeatherRemoteRepository remote) {
        if (INSTANCE == null) {
            INSTANCE = new WeatherRepositoryManager(local, remote);
            return INSTANCE;
        }

        return INSTANCE;
    }

    @Override
    public void getCurrentWeather(double lat, double lon, Action1<WeatherResponse> success, Action1<Throwable> failure, Action0 complete) {
        weatherRemoteRepository.getCurrentWeather(lat, lon, success, failure, complete);
    }

    @Override
    public void getPlaceByName(String name, Action1<Places> success, Action1<Throwable> failure, Action0 complete) {
        weatherRemoteRepository.getPlaceByName(name, success, failure, complete);
    }

    @Override
    public void getWeatherForFiveDay(double lat, double lon, Action1<ResponseManyDayWeather> success, Action1<Throwable> failure, Action0 complete) {
        weatherRemoteRepository.getWeatherForFiveDay(lat, lon, success, failure, complete);
    }

    @Override
    public void getPlaceId(String placeId, Action1<PlaceDetails> success, Action1<Throwable> failure, Action0 complete) {
        weatherRemoteRepository.getPlaceId(placeId, success, failure, complete);

    }
}
