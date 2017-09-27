package com.example.dmitro.weatherapp.screen.current_weather.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.data.model.weather.WeatherResponse;
import com.example.dmitro.weatherapp.screen.forecas_list.ForecastListActivity;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WeatherListFragment extends Fragment {
    public static final String FRAGMENT_KEY = "WeatherListFragment";

    private WeatherRecyclerAdapter weatherRecyclerAdapter;

    @BindView(R.id.weather_list_rv)
    public RecyclerView recyclerView;

    public WeatherListFragment() {
        // Required empty public constructor
    }


    public void notifyField() {
        if (getArguments() != null) {
            ArrayList<HashMap<String, ArrayList<WeatherResponse>>> responseManyDayWeather = (ArrayList<HashMap<String, ArrayList<WeatherResponse>>>) getArguments().getSerializable(FRAGMENT_KEY);
            initAdapter(responseManyDayWeather);

        }
    }

    private void initAdapter(ArrayList<HashMap<String, ArrayList<WeatherResponse>>> wr) {
        weatherRecyclerAdapter.updateData(wr);

    }

    public static WeatherListFragment newInstance(String param1, String param2) {
        WeatherListFragment fragment = new WeatherListFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            intent.putExtra(ForecastListActivity.KEY_WEATHER, (HashMap<String, ArrayList<WeatherResponse>>) dayWeather);
            startActivity(intent);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(weatherRecyclerAdapter);

    }


}
