package com.example.dmitro.weatherapp.screen.navigation;

import android.content.SharedPreferences;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.WeatherApp;
import com.example.dmitro.weatherapp.data.repository.WeatherRepositoryManager;
import com.example.dmitro.weatherapp.utils.Injection;

/**
 * Created by dmitro on 29.09.17.
 */

public class NavigationPresenter implements NavigationContract.Presenter {
    private NavigationContract.View view;
    private WeatherRepositoryManager weatherRepositoryManager = Injection.provideManager();
    private SharedPreferences sharedPreferences;


    public NavigationPresenter(NavigationContract.View view, SharedPreferences sharedPreferences) {
        this.view = view;
        view.setPresenter(this);
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void init() {


    }

    @Override
    public void getCurrentUser() {
        if (!sharedPreferences.getString(WeatherApp.getInstance().getString(R.string.google_auth_token), "").isEmpty()) {
            weatherRepositoryManager.getLocalUser(sRemote -> {
                view.showUserInformation(sRemote);
            }, f -> {
            }, () -> {
            });
        } else {
            weatherRepositoryManager.getRemoteUser(s -> {
                view.showUserInformation(s);
                weatherRepositoryManager.cacheUserData(s);
            }, f -> {
                weatherRepositoryManager.getLocalUser(sLocacl -> {
                    view.showUserInformation(sLocacl);
                }, fLocal -> {
                }, () -> {
                });

            }, () -> {
            });
        }

    }
}
