package com.example.dmitro.weatherapp.screen.select_city;

import com.example.dmitro.weatherapp.data.model.geo.places.Places;
import com.example.dmitro.weatherapp.screen.BasePresenter;
import com.example.dmitro.weatherapp.screen.BaseView;

/**
 * Created by dmitro on 26.09.17.
 */

public class CityChoiceContract {
    public interface View extends BaseView<Presenter> {

        public void showNewPlaces(Places places);

        public void showWeatherSelectedCity(double lat, double lng);

    }

    public interface Presenter extends BasePresenter {

        public void getPlacesByWord(String place);

        public void getDetailsPlace(String placeId);
    }
}
