package com.example.dmitro.weatherapp.screen.authorization;

import android.content.SharedPreferences;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.WeatherApp;
import com.example.dmitro.weatherapp.data.model.weather.Weather;
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
        editor.putString(WeatherApp.getInstance().getString(R.string.google_token), token);
        editor.commit();

    }

    @Override
    public void trySignIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            view.openBaseScreen();
        }else if(!pref.getString(WeatherApp.getInstance().getString(R.string.google_token),"").isEmpty()){
            view.openBaseScreen();
        }
    }

    @Override
    public void signInFacebook() {

    }

    @Override
    public void signInGoogle() {

    }
}
