package com.example.dmitro.weatherapp.screen.weather.fragment;

import com.example.dmitro.weatherapp.data.model.weather.current.WeatherResponse;
import com.example.dmitro.weatherapp.databinding.FragmentWeatherDetailsBinding;
import com.example.dmitro.weatherapp.utils.ImageLoader;
import com.example.dmitro.weatherapp.utils.MyUtil;

/** Fills the shared "weather details" layout; used by the current-weather fragment and the forecast list rows. */
public final class WeatherDetailsBinder {
    public static final String POSTFIX_BY_TEMP_PERCENT = " %";
    public static final String POSTFIX_BY_TEMP_MS = " m/s";

    private WeatherDetailsBinder() {
    }

    public static void bind(FragmentWeatherDetailsBinding binding, WeatherResponse wr) {
        if (wr == null || wr.getMain() == null) {
            return;
        }
        binding.temperatureTv.setText(wr.getMain().getTemp() + MyUtil.POSTFIX_BY_TEMP_CELSIUS);
        binding.humidityTv.setText(wr.getMain().getHumidity() + POSTFIX_BY_TEMP_PERCENT);
        if (wr.getWind() != null) {
            binding.windTv.setText(wr.getWind().getSpeed() + POSTFIX_BY_TEMP_MS);
        }
        binding.diapasonTempTv.setText(MyUtil.createTemperatureDiapasonString(wr.getMain().getTempMax(), wr.getMain().getTempMin()));
        if (wr.getWeather() != null && !wr.getWeather().isEmpty()) {
            ImageLoader.loadWeatherIcon(binding.weatherIconIv, wr.getWeather().get(0).getIcon());
        }
    }
}
