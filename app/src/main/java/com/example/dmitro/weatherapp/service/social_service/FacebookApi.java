package com.example.dmitro.weatherapp.service.social_service;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.dmitro.weatherapp.WeatherApp;
import com.example.dmitro.weatherapp.data.model.weather.current.WeatherResponse;
import com.example.dmitro.weatherapp.data.repository.WeatherRepositoryManager;
import com.example.dmitro.weatherapp.utils.ImageLoader;
import com.example.dmitro.weatherapp.utils.Injection;
import com.facebook.FacebookSdk;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

public class FacebookApi {

    public static final String TEMPERATURE = "t°: ";
    public static final String LOCATION = "location: ";

    public static void sharePhoto(String caption, String photoUrl) {
        Glide.with(WeatherApp.getInstance()).asBitmap().load(photoUrl).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                sharePhoto(caption, bitmap);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
            }
        });
    }

    public static void sharePhoto(String caption, Bitmap bitmap) {
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .setCaption(caption)
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        ShareApi.share(content, null);
    }

    /** Shares the cached current weather; returns false when nothing has been loaded yet. */
    public static boolean shareWeather() {
        if (!FacebookSdk.isInitialized()) {
            return false;
        }
        WeatherRepositoryManager manager = Injection.provideManager();
        WeatherResponse weather = manager.getCacheWeather();
        if (weather == null || weather.getWeather() == null || weather.getWeather().isEmpty()) {
            return false;
        }
        String url = ImageLoader.weatherIconUrl(weather.getWeather().get(0).getIcon());
        String caption = TEMPERATURE + weather.getMain().getTemp() + "  " + LOCATION + weather.getName();
        sharePhoto(caption, url);
        return true;
    }
}
