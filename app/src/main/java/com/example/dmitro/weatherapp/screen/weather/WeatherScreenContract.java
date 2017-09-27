package com.example.dmitro.weatherapp.screen.weather;

import com.example.dmitro.weatherapp.data.model.weather.WeatherResponse;
import com.example.dmitro.weatherapp.data.model.weather.many_day.ResponseManyDayWeather;
import com.example.dmitro.weatherapp.screen.BaseView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dmitro on 25.09.17.
 */

public class WeatherScreenContract {
    interface WeatherScreenView extends BaseView<WeatherScreenPresenter> {

        public void showWeatherDetails(WeatherResponse response);

        public void showCityNameInToolbar(String name);

        public void showWeatherList(LinkedList<HashMap<String, List<WeatherResponse>>> responseManyDayWeather);
    }

    interface WeatherScreenPresenter {
        public void getWeather();

        public void getWeatherForFiveDays();

    }
}
