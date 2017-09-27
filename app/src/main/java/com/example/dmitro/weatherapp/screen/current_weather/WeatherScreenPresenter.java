package com.example.dmitro.weatherapp.screen.current_weather;

import com.example.dmitro.weatherapp.data.repository.WeatherRepositoryManager;
import com.example.dmitro.weatherapp.utils.Injection;
import com.example.dmitro.weatherapp.utils.MyUtil;

/**
 * Created by dmitro on 25.09.17.
 */

public class WeatherScreenPresenter implements WeatherScreenContract.WeatherScreenPresenter {
    private double lat;
    private double lon;
    private WeatherScreenContract.WeatherScreenView view;
    private WeatherRepositoryManager weatherRepositoryManager;

    public WeatherScreenPresenter(double lat, double lon, WeatherScreenContract.WeatherScreenView view) {
        this.lat = lat;
        this.lon = lon;
        this.view = view;
        view.setPresenter(this);
        weatherRepositoryManager = Injection.provideManager();
    }

    @Override
    public void getWeather() {
        weatherRepositoryManager.getCurrentWeather(this.lat, this.lon, s -> {
            view.showWeatherDetails(s);
            view.showCityNameInToolbar(s.getName());
        }, f -> {
        }, () -> {
        });

    }

    @Override
    public void getWeatherForFiveDays() {
        weatherRepositoryManager.getWeatherForFiveDay(this.lat, this.lon, s -> {
            view.showWeatherList(MyUtil.groupByDays(s));
        }, f -> {
        }, () -> {
        });
    }
}
