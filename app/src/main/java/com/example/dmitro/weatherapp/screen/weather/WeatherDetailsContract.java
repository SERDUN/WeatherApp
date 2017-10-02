package com.example.dmitro.weatherapp.screen.weather;

import com.example.dmitro.weatherapp.data.model.weather.current.WeatherResponse;
import com.example.dmitro.weatherapp.screen.BaseView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dmitro on 25.09.17.
 */

public class WeatherDetailsContract {
    interface View extends BaseView<Presenter> {

        void showProgressBar(boolean show);

        void showContent(boolean show);

        void showWeatherDetails(WeatherResponse response);

        void showCityNameInToolbar(String name);

        void showFailure();

        void showWeatherList(ArrayList<HashMap<String, ArrayList<WeatherResponse>>> responseManyDayWeather);
    }

    interface Presenter {
        void init();

        void getCurrentWeather();

        void getWeatherForLanLon(double lat, double lon);

        void getCurrentWeatherByFiveDays();

        void getWeatherByFiveDayForLatLon(double lat, double lon);


        WeatherResponse getCacheWeather();


    }
}
