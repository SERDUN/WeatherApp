package com.example.dmitro.weatherapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.WeatherApp;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by dmitro on 04.10.17.
 */

public class SocialProfileUtils {
    public static SocialType getCurrentSocialProfile(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("token", MODE_PRIVATE);
        if (!sharedPreferences.getString(WeatherApp.getInstance().getString(R.string.google_auth_token), "").isEmpty()) {
            return SocialType.GOOGLE;
        } else if (!sharedPreferences.getString(WeatherApp.getInstance().getString(R.string.facebook_auth_token), "").isEmpty()) {
            return SocialType.FACEBOOK;
        }
        return SocialType.OTHER;
    }
}
