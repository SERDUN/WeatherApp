package com.example.dmitro.weatherapp.data.repository;

import com.example.dmitro.weatherapp.data.model.geo.placeDetails.PlaceDetails;
import com.example.dmitro.weatherapp.data.model.geo.places.Places;
import com.example.dmitro.weatherapp.data.model.social.User;
import com.example.dmitro.weatherapp.data.model.weather.current.WeatherResponse;
import com.example.dmitro.weatherapp.data.model.weather.many_day.ResponseManyDayWeather;
import com.example.dmitro.weatherapp.data.repository.local.WeatherLocalRepository;
import com.example.dmitro.weatherapp.data.repository.remote.RemoteRepository;
import com.example.dmitro.weatherapp.utils.callback.Action0;
import com.example.dmitro.weatherapp.utils.callback.Action1;

/**
 * Created by dmitro on 25.09.17.
 */

public class WeatherRepositoryManager implements WeatherDataSource, SocialDataSources {
    private static WeatherDataSource INSTANCE = null;
    private final WeatherLocalRepository localRepository;
    private final RemoteRepository remoteRepository;

    private WeatherResponse cacheWeather;
    private ResponseManyDayWeather cacheManyDayWeather;

    private WeatherRepositoryManager(WeatherLocalRepository localRepository, RemoteRepository remoteRepository) {
        this.localRepository = localRepository;
        this.remoteRepository = remoteRepository;
    }

    public static WeatherDataSource getInstance(WeatherLocalRepository local, RemoteRepository remote) {
        if (INSTANCE == null) {
            INSTANCE = new WeatherRepositoryManager(local, remote);
            return INSTANCE;
        }

        return INSTANCE;
    }

    @Override
    public WeatherResponse getCacheWeather() {
        return this.cacheWeather;
    }

    @Override
    public ResponseManyDayWeather getCacheManyDayWeather() {
        return cacheManyDayWeather;
    }

    @Override
    public void getCurrentWeather(double lat, double lon, Action1<WeatherResponse> success, Action1<Throwable> failure, Action0 complete) {

        remoteRepository.getCurrentWeather(lat, lon, new Action1<WeatherResponse>() {
            @Override
            public void call(WeatherResponse o) {
                cacheWeather = o;
                success.call(o);
            }
        }, failure, complete);
    }

    @Override
    public void getPlaceByName(String name, Action1<Places> success, Action1<Throwable> failure, Action0 complete) {
        remoteRepository.getPlaceByName(name, success, failure, complete);
    }

    @Override
    public void getWeatherForFiveDay(double lat, double lon, Action1<ResponseManyDayWeather> success, Action1<Throwable> failure, Action0 complete) {
        remoteRepository.getWeatherForFiveDay(lat, lon, s -> {
            cacheManyDayWeather = s;
            success.call(s);
        }, failure, complete);
    }

    @Override
    public void getPlaceId(String placeId, Action1<PlaceDetails> success, Action1<Throwable> failure, Action0 complete) {
        remoteRepository.getPlaceId(placeId, success, failure, complete);

    }

    @Override
    public void cacheUserData(User user) {
        localRepository.cacheUserData(user);
    }

    @Override
    public void getLocalUser(Action1<User> success, Action1<Throwable> failure, Action0 complete) {
        localRepository.getLocalUser(success, failure, complete);
    }

    @Override
    public void getRemoteUser(Action1<User> success, Action1<Throwable> failure, Action0 complete) {
        remoteRepository.getRemoteUser(success, failure, complete);
    }
}
