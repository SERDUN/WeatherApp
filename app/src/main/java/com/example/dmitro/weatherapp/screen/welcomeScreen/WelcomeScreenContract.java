package com.example.dmitro.weatherapp.screen.welcomeScreen;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.example.dmitro.weatherapp.screen.BaseView;
import com.google.android.gms.location.FusedLocationProviderClient;

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
