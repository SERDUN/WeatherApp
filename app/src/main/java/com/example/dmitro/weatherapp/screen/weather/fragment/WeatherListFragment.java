package com.example.dmitro.weatherapp.screen.weather.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.data.model.weather.current.WeatherResponse;
import com.example.dmitro.weatherapp.screen.forecast_list.ForecastListActivity;
import com.example.dmitro.weatherapp.data.loader.base.Response;
import com.example.dmitro.weatherapp.data.loader.CurrentWeatherManyDayLoader;
import com.example.dmitro.weatherapp.utils.MyUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WeatherListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Response> {
    public static final String FRAGMENT_KEY = "WeatherListFragment";

    private WeatherRecyclerAdapter weatherRecyclerAdapter;

    @BindView(R.id.weather_list_rv)
    public RecyclerView recyclerView;


    private Loader<Response> loader;

    public WeatherListFragment() {
        // Required empty public constructor
    }


    public static WeatherListFragment newInstance() {
        WeatherListFragment fragment = new WeatherListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loader = getLoaderManager().initLoader(FRAGMENT_KEY.hashCode(), savedInstanceState, this);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_list, container, false);
        ButterKnife.bind(this, view);
        initRecyclerView();
        return view;
    }


    private void initRecyclerView() {
        weatherRecyclerAdapter = new WeatherRecyclerAdapter(new ArrayList<>(), dayWeather -> {
            Intent intent = new Intent(getContext(), ForecastListActivity.class);
            intent.putExtra(getString(R.string.weather_forecast_for_day), (HashMap<String, ArrayList<WeatherResponse>>) dayWeather);
            startActivity(intent);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(weatherRecyclerAdapter);


    }


    @Override
    public Loader<Response> onCreateLoader(int id, Bundle args) {
        loader = new CurrentWeatherManyDayLoader(getContext());
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Response> loader, Response data) {
        if (data.getTypedAnswer() != null) {
            if (getArguments() == null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(FRAGMENT_KEY, data.getTypedAnswer());

            } else {
                getArguments().putSerializable(FRAGMENT_KEY, data.getTypedAnswer());
            }
            weatherRecyclerAdapter.updateData(MyUtil.groupByDays((data.getTypedAnswer())));

        }
    }

    @Override
    public void onLoaderReset(Loader<Response> loader) {

    }
}
