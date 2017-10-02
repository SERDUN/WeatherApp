package com.example.dmitro.weatherapp.screen.select_city;

import com.example.dmitro.weatherapp.data.model.geo.places.Places;
import com.example.dmitro.weatherapp.screen.BasePresenter;
import com.example.dmitro.weatherapp.screen.BaseView;

/**
 * Created by dmitro on 26.09.17.
 */

public class CityChoiceContract {
    public interface View extends BaseView<Presenter> {

         void showNewPlaces(Places places);

         void showWeatherSelectedCity(double lat, double lng);

    }

    public interface Presenter extends BasePresenter {

         void getPlacesByWord(String place);

         void getDetailsPlace(String placeId);
    }
}
