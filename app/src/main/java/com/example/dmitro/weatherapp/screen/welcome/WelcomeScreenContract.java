package com.example.dmitro.weatherapp.screen.welcome;

import android.location.Location;

import com.example.dmitro.weatherapp.screen.BaseView;

/**
 * Created by dmitro on 25.09.17.
 */

public class WelcomeScreenContract {

    interface WelcomeScreenView extends BaseView<WelcomeScreenPresenter> {
        public void openWeatherDetails(double lat,double lon);
    }

    interface WelcomeScreenPresenter {
        public void openWeatherActivity(Location location);
    }
}
