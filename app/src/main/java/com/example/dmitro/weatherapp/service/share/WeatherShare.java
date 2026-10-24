package com.example.dmitro.weatherapp.service.share;

import android.app.Activity;

import androidx.core.app.ShareCompat;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.data.model.weather.current.WeatherResponse;
import com.example.dmitro.weatherapp.utils.Injection;

/** Shares the cached current weather as text through the system share sheet. */
public final class WeatherShare {
    private WeatherShare() {
    }

    /** Returns false when no weather has been loaded yet. */
    public static boolean shareCurrentWeather(Activity activity) {
        WeatherResponse weather = Injection.provideManager().getCacheWeather();
        if (weather == null || weather.getMain() == null || weather.getWeather() == null || weather.getWeather().isEmpty()) {
            return false;
        }
        String text = activity.getString(R.string.share_weather_text,
                weather.getName(), weather.getMain().getTemp(), weather.getWeather().get(0).getDescription());
        new ShareCompat.IntentBuilder(activity)
                .setType("text/plain")
                .setText(text)
                .setChooserTitle(R.string.shared_weather)
                .startChooser();
        return true;
    }
}
