package com.example.dmitro.weatherapp.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.dmitro.weatherapp.BuildConfig;

/** Thin wrapper over Glide for the OpenWeatherMap icon URLs used across the screens. */
public final class ImageLoader {
    private static final String ICON_SUFFIX = ".png";

    private ImageLoader() {
    }

    public static String weatherIconUrl(String icon) {
        return BuildConfig.ICON_WEATHER_URL + icon + ICON_SUFFIX;
    }

    public static void loadWeatherIcon(ImageView target, String icon) {
        Glide.with(target).load(weatherIconUrl(icon)).into(target);
    }

    public static void loadAvatar(ImageView target, String url) {
        Glide.with(target).load(url).circleCrop().into(target);
    }
}
