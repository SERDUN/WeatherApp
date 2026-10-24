package com.example.dmitro.weatherapp.screen.weather;

import android.annotation.SuppressLint;
import android.location.Location;

import com.example.dmitro.weatherapp.WeatherApp;
import com.example.dmitro.weatherapp.data.model.weather.current.WeatherResponse;
import com.example.dmitro.weatherapp.data.repository.WeatherRepositoryManager;
import com.example.dmitro.weatherapp.utils.Injection;
import com.example.dmitro.weatherapp.utils.MyUtil;
import com.example.dmitro.weatherapp.utils.callback.Action1;
import com.google.android.gms.location.LocationServices;

public class WeatherDetailsPresenter implements WeatherDetailsContract.Presenter {
    private final WeatherDetailsContract.View view;
    private final WeatherRepositoryManager weatherRepositoryManager;

    public WeatherDetailsPresenter(WeatherDetailsContract.View view) {
        this.view = view;
        view.setPresenter(this);
        weatherRepositoryManager = Injection.provideManager();
    }

    /** The toolbar lives inside the content view, so it stays visible even while loading or after a failure. */
    @Override
    public void init() {
        view.showProgressBar(true);
        view.showContent(true);
    }

    @Override
    public void getCurrentWeatherByFiveDays() {
        withLastLocation(location -> getWeatherByFiveDayForLatLon(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void getWeatherByFiveDayForLatLon(double lat, double lon) {
        weatherRepositoryManager.getWeatherForFiveDay(lat, lon,
                s -> view.showWeatherList(MyUtil.groupByDays(s)),
                f -> view.showFailure(),
                () -> view.showProgressBar(false));
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
            view.showContent(true);
        }, f -> view.showFailure(), () -> view.showProgressBar(false));
    }

    @Override
    public void getCurrentWeather() {
        withLastLocation(location -> getWeatherForLanLon(location.getLatitude(), location.getLongitude()));
    }

    /** The view is responsible for asking the location permission before calling the presenter. */
    @SuppressLint("MissingPermission")
    private void withLastLocation(Action1<Location> action) {
        try {
            LocationServices.getFusedLocationProviderClient(WeatherApp.getInstance())
                    .getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location == null) {
                            view.showProgressBar(false);
                            view.showFailure();
                        } else {
                            action.call(location);
                        }
                    })
                    .addOnFailureListener(e -> {
                        view.showProgressBar(false);
                        view.showFailure();
                    });
        } catch (SecurityException e) {
            view.showProgressBar(false);
            view.showFailure();
        }
    }
}
