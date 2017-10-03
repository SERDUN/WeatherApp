package com.example.dmitro.weatherapp.data.repository.local;

import android.app.Application;

import com.example.dmitro.weatherapp.data.model.geo.placeDetails.PlaceDetails;
import com.example.dmitro.weatherapp.data.model.geo.places.Places;
import com.example.dmitro.weatherapp.data.model.social.UserFacebook;
import com.example.dmitro.weatherapp.data.model.weather.current.WeatherResponse;
import com.example.dmitro.weatherapp.data.model.weather.many_day.ResponseManyDayWeather;
import com.example.dmitro.weatherapp.data.repository.WeatherDataSource;
import com.example.dmitro.weatherapp.utils.callback.Action0;
import com.example.dmitro.weatherapp.utils.callback.Action1;

import io.realm.Realm;

/**
 * Created by dmitro on 25.09.17.
 */

public class WeatherLocalRepository implements WeatherDataSource {


    @Override
    public WeatherResponse getCacheWeather() {
        return null;
    }

    @Override
    public ResponseManyDayWeather getCacheManyDayWeather() {
        return null;
    }

    @Override
    public void getCurrentWeather(double lat, double lon, Action1<WeatherResponse> success, Action1<Throwable> failure, Action0 complete) {

    }

    @Override
    public void getPlaceByName(String name, Action1<Places> success, Action1<Throwable> failure, Action0 complete) {

    }

    @Override
    public void getWeatherForFiveDay(double lat, double lon, Action1<ResponseManyDayWeather> success, Action1<Throwable> failure, Action0 complete) {

    }

    @Override
    public void getPlaceId(String placeId, Action1<PlaceDetails> success, Action1<Throwable> failure, Action0 complete) {

    }

    @Override
    public void cacheUserData(UserFacebook userFacebook) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(userFacebook);
        realm.commitTransaction();
        realm.close();


    }
}
