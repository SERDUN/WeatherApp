package com.example.dmitro.weatherapp.screen.weatherScreen;

import com.example.dmitro.weatherapp.data.model.weather.WeatherResponse;
import com.example.dmitro.weatherapp.screen.BaseView;

/**
 * Created by dmitro on 25.09.17.
 */

public class WeatherScreenContract {
    interface WeatherScreenView extends BaseView<WeatherScreenPresenter> {

        public void showWeatherDetails(WeatherResponse response);

        public void showFailureMessage();
    }

    interface WeatherScreenPresenter {
        public void getWeather();
    }
}
