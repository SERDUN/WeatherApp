package com.example.dmitro.weatherapp.service.social_service;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.WeatherApp;
import com.example.dmitro.weatherapp.data.repository.WeatherRepositoryManager;
import com.example.dmitro.weatherapp.utils.Injection;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import static com.example.dmitro.weatherapp.screen.weather.fragment.WeatherCurrentDetailsFragment.POSTFIX_BY_ICON_URL;

/**
 * Created by dmitro on 02.10.17.
 */

public class FacebookApi {

    //// TODO: 02.10.17 add error handling

    public static void sharePhoto(String caption, String photoUrl) {
//        String url = WeatherApp.getInstance().getBaseContext().getString(R.string.ICON_WEATHER_URL) + presenter.getCacheWeather().getWeather().get(0).getIcon() + POSTFIX_BY_ICON_URL;
        Picasso.with(WeatherApp.getInstance().getBaseContext()).load(photoUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(bitmap)
                        .setCaption(caption)
                        .build();

                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();

                ShareApi.share(content, null);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

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


    public static final String TEMPERATURE = "t°: ";
    public static final String LOCATION = "location: ";

    public static void shareWeather() {
        WeatherRepositoryManager serviceManager = Injection.provideManager();
        String url = WeatherApp.getInstance().getBaseContext().getString(R.string.icon_weather_url) + serviceManager.getCacheWeather().getWeather().get(0).getIcon() + POSTFIX_BY_ICON_URL;
        String caption = TEMPERATURE + serviceManager.getCacheWeather().getMain().getTemp() +"  "+LOCATION + serviceManager.getCacheWeather().getName();
        sharePhoto(caption, url);
    }
}
