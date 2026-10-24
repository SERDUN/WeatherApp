package com.example.dmitro.weatherapp.screen.weather.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.data.model.weather.current.WeatherResponse;
import com.example.dmitro.weatherapp.data.model.weather.many_day.ResponseManyDayWeather;
import com.example.dmitro.weatherapp.databinding.FragmentWeatherListBinding;
import com.example.dmitro.weatherapp.screen.forecast_list.ForecastListActivity;
import com.example.dmitro.weatherapp.service.weather_service.weather_loader.base.Response;
import com.example.dmitro.weatherapp.service.weather_service.weather_loader.realization_loader.CurrentWeatherManyDayLoader;
import com.example.dmitro.weatherapp.utils.MyUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class WeatherListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Response> {
    public static final String FRAGMENT_KEY = "WeatherListFragment";

    private WeatherRecyclerAdapter weatherRecyclerAdapter;
    private FragmentWeatherListBinding binding;

    public WeatherListFragment() {
    }

    public static WeatherListFragment newInstance() {
        return new WeatherListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoaderManager.getInstance(this).initLoader(FRAGMENT_KEY.hashCode(), savedInstanceState, this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeatherListBinding.inflate(inflater, container, false);
        initRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressWarnings("unchecked")
    private void initRecyclerView() {
        weatherRecyclerAdapter = new WeatherRecyclerAdapter(new ArrayList<>(), dayWeather -> {
            Intent intent = new Intent(getContext(), ForecastListActivity.class);
            intent.putExtra(getString(R.string.weather_forecast_for_day), (HashMap<String, ArrayList<WeatherResponse>>) dayWeather);
            startActivity(intent);
        });
        binding.weatherListRv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.weatherListRv.setAdapter(weatherRecyclerAdapter);
    }

    @NonNull
    @Override
    public Loader<Response> onCreateLoader(int id, Bundle args) {
        return new CurrentWeatherManyDayLoader(requireContext());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Response> loader, Response data) {
        ResponseManyDayWeather weather = data.getTypedAnswer();
        if (weather != null && weatherRecyclerAdapter != null) {
            weatherRecyclerAdapter.updateData(MyUtil.groupByDays(weather));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Response> loader) {
    }
}
