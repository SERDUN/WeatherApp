package com.example.dmitro.weatherapp.screen.forecast_list;

import com.example.dmitro.weatherapp.data.model.weather.current.WeatherResponse;
import com.example.dmitro.weatherapp.screen.BasePresenter;
import com.example.dmitro.weatherapp.screen.BaseView;

import java.util.ArrayList;

/**
 * Created by dmitro on 27.09.17.
 */

public class ForecastListContract {
    public interface View extends BaseView<ForecastListContract.Presenter> {
        public void showWeather(ArrayList<WeatherResponse> weatherResponses);

        public void showDayInToolbar(String name);

    }

    public interface Presenter extends BasePresenter {
        public void init();
    }
}