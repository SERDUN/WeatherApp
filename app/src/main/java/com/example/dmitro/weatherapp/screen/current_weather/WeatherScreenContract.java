package com.example.dmitro.weatherapp.screen.current_weather;

import com.example.dmitro.weatherapp.data.model.weather.WeatherResponse;
import com.example.dmitro.weatherapp.screen.BaseView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dmitro on 25.09.17.
 */

public class WeatherScreenContract {
    interface WeatherScreenView extends BaseView<WeatherScreenPresenter> {

        public void showProgressBar(boolean show);



        public void showContent(boolean show);

        public void showWeatherDetails(WeatherResponse response);

        public void showCityNameInToolbar(String name);

        public void showFailure();

        public void showWeatherList(ArrayList<HashMap<String, ArrayList<WeatherResponse>>> responseManyDayWeather);
    }

    interface WeatherScreenPresenter {
        void init();

        public void getWeather();

        public void getWeatherForFiveDays();


    }
}
