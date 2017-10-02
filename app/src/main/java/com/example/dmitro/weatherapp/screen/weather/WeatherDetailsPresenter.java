package com.example.dmitro.weatherapp.screen.weather;

import android.location.Location;

import com.example.dmitro.weatherapp.WeatherApp;
import com.example.dmitro.weatherapp.data.model.weather.current.WeatherResponse;
import com.example.dmitro.weatherapp.data.repository.WeatherRepositoryManager;
import com.example.dmitro.weatherapp.utils.Injection;
import com.example.dmitro.weatherapp.utils.MyUtil;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


/**
 * Created by dmitro on 25.09.17.
 */

public class WeatherDetailsPresenter implements WeatherDetailsContract.Presenter {
    private WeatherDetailsContract.View view;
    private WeatherRepositoryManager weatherRepositoryManager;

    public WeatherDetailsPresenter(WeatherDetailsContract.View view) {
        this.view = view;
        view.setPresenter(this);
        weatherRepositoryManager = Injection.provideManager();
    }

    @Override
    public void init() {
        view.showProgressBar(true);
        view.showContent(false);
    }


    @Override
    public void getCurrentWeatherByFiveDays() {
        LocationServices.getFusedLocationProviderClient(WeatherApp.getInstance().getBaseContext()).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                getWeatherByFiveDayForLatLon(location.getLatitude(), location.getLongitude());
            }
        });
    }

    @Override
    public void getWeatherByFiveDayForLatLon(double lat, double lon) {
        weatherRepositoryManager.getWeatherForFiveDay(lat, lon, s -> {
            view.showWeatherList(MyUtil.groupByDays(s));
        }, f -> {
        }, () -> {
            view.showProgressBar(false);
        });
    }

    @Override
    public WeatherResponse getCacheWeather() {
        return weatherRepositoryManager.getCacheWeather();
    }

    @Override
    public void getWeatherForLanLon(double lat, double lon) {
        weatherRepositoryManager.getCurrentWeather(lat, lon, s -> {
            view.showWeatherDetails(s);
            view.showCityNameInToolbar(s.getName());
//            view.showContent(true);


        }, f -> {
//            view.showFailure();
            view.showContent(false);
        }, () -> {
            view.showProgressBar(false);
            view.showContent(true);
        });
    }


    @Override
    public void getCurrentWeather() {
        LocationServices.getFusedLocationProviderClient(WeatherApp.getInstance().getBaseContext()).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                getWeatherForLanLon(location.getLatitude(), location.getLongitude());
            }
        });
    }

}





