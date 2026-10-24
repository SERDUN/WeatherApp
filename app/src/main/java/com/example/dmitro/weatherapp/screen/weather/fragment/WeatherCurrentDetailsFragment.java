package com.example.dmitro.weatherapp.screen.weather.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.example.dmitro.weatherapp.data.model.weather.current.WeatherResponse;
import com.example.dmitro.weatherapp.databinding.FragmentWeatherDetailsBinding;
import com.example.dmitro.weatherapp.service.weather_service.weather_loader.base.Response;
import com.example.dmitro.weatherapp.service.weather_service.weather_loader.realization_loader.CurrentWeatherLoader;

public class WeatherCurrentDetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Response> {

    public static final String FRAGMENT_KEY = "WeatherCurrentDetailsFragment";

    private FragmentWeatherDetailsBinding binding;

    public WeatherCurrentDetailsFragment() {
    }

    public static WeatherCurrentDetailsFragment newInstance() {
        return new WeatherCurrentDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoaderManager.getInstance(this).initLoader(FRAGMENT_KEY.hashCode(), savedInstanceState, this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @NonNull
    @Override
    public Loader<Response> onCreateLoader(int id, Bundle args) {
        return new CurrentWeatherLoader(requireContext());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Response> loader, Response data) {
        WeatherResponse weather = data.getTypedAnswer();
        if (weather != null && binding != null) {
            WeatherDetailsBinder.bind(binding, weather);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Response> loader) {
    }
}
