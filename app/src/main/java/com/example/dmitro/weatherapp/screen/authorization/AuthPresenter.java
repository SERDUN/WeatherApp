package com.example.dmitro.weatherapp.screen.authorization;

import android.content.SharedPreferences;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.WeatherApp;
import com.example.dmitro.weatherapp.screen.authorization.AuthContract.Presenter;
import com.facebook.AccessToken;

/**
 * Created by dmitro on 28.09.17.
 */

public class AuthPresenter implements Presenter {
    private AuthContract.View view;
    private SharedPreferences pref;


    public AuthPresenter(AuthContract.View view, SharedPreferences sharedPreferences) {
        this.view = view;
        view.setPresenter(this);
        this.pref = sharedPreferences;
    }


    @Override
    public void saveTokenForGoogle(String token) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(WeatherApp.getInstance().getString(R.string.google_auth_token), token);
        editor.apply();

    }

    @Override
    public void saveTokenForFacebook(String token) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(WeatherApp.getInstance().getString(R.string.facebook_auth_token), token);
        editor.apply();
    }

    @Override
    public void trySignIn() {
        if (!pref.getString(WeatherApp.getInstance().getString(R.string.facebook_auth_token), "").isEmpty()) {
            view.openBaseScreen();
        } else if (!pref.getString(WeatherApp.getInstance().getString(R.string.google_auth_token), "").isEmpty()) {
            view.openBaseScreen();
        }
    }

}
