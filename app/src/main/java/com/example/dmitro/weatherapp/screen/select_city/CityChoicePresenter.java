package com.example.dmitro.weatherapp.screen.select_city;

import com.example.dmitro.weatherapp.data.repository.WeatherRepositoryManager;
import com.example.dmitro.weatherapp.utils.Injection;

/**
 * Created by dmitro on 26.09.17.
 */

public class CityChoicePresenter implements CityChoiceContract.Presenter {
    private WeatherRepositoryManager manager;
    private CityChoiceContract.View view;

    public CityChoicePresenter(CityChoiceContract.View view) {
        this.view = view;
        view.setPresenter(this);
        manager = Injection.provideManager();
    }

    @Override
    public void getPlacesByWord(String place) {
        manager.getPlaceByName(place, s -> {
            view.showNewPlaces(s);
        }, f -> {
        }, () -> {
        });
    }

    @Override
    public void getDetailsPlace(String placeId) {
        manager.getPlaceId(placeId, s -> {
            view.showWeatherSelectedCity(s.getResult().getGeometry().getLocation().getLat(), s.getResult().getGeometry().getLocation().getLng());
        }, f -> {
        }, () -> {
        });
    }
}
