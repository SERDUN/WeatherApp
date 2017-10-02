package com.example.dmitro.weatherapp.screen.forecast_list;

import com.example.dmitro.weatherapp.data.model.weather.current.WeatherResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by dmitro on 27.09.17.
 */

public class ForecastListPresenter implements ForecastListContract.Presenter {
    private HashMap<String, ArrayList<WeatherResponse>> weather;
    private ForecastListContract.View view;

    public ForecastListPresenter(HashMap<String, ArrayList<WeatherResponse>> weather, ForecastListContract.View view) {
        this.weather = weather;
        this.view = view;
        view.setPresenter(this);
    }


    @Override
    public void init() {
        Iterator it = weather.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            view.showDayInToolbar(pair.getKey().toString());
            view.showWeather(weather.get(pair.getKey().toString()));
        }

    }
}
