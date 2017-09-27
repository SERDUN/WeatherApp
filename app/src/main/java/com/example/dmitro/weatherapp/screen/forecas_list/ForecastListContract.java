package com.example.dmitro.weatherapp.screen.forecas_list;

import com.example.dmitro.weatherapp.data.model.geo.places.Places;
import com.example.dmitro.weatherapp.data.model.weather.WeatherResponse;
import com.example.dmitro.weatherapp.screen.BasePresenter;
import com.example.dmitro.weatherapp.screen.BaseView;

import java.util.LinkedList;

/**
 * Created by dmitro on 27.09.17.
 */

public class ForecastListContract {
    public interface View extends BaseView<ForecastListContract.Presenter> {
        public void showWeather(LinkedList<WeatherResponse> weatherResponses);

        public void showDayInToolbar(String name);

    }

    public interface Presenter extends BasePresenter {
        public void init();
    }
}