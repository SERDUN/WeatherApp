package com.example.dmitro.weatherapp.screen.welcome;

import android.content.Context;
import android.location.Location;


/**
 * Created by dmitro on 25.09.17.
 */

public class WelcomeScreenPresenter implements WelcomeScreenContract.WelcomeScreenPresenter {
    private WelcomeScreenContract.WelcomeScreenView view;

    public WelcomeScreenPresenter(WelcomeScreenContract.WelcomeScreenView view, Context context) {
        this.view = view;

    }

    @Override
    public void openWeatherActivity(Location location) {
        view.openWeatherDetails(location.getLatitude(), location.getLongitude());
    }
}
