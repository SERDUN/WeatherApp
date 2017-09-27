package com.example.dmitro.weatherapp.screen.weather.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.data.model.weather.WeatherResponse;
import com.example.dmitro.weatherapp.data.model.weather.many_day.ResponseManyDayWeather;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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
            LinkedList<HashMap<String, List<WeatherResponse>>> responseManyDayWeather = (LinkedList<HashMap<String, List<WeatherResponse>>>) getArguments().getSerializable(FRAGMENT_KEY);
            initAdapter(responseManyDayWeather);

        }
    }

    private void initAdapter(LinkedList<HashMap<String, List<WeatherResponse>>> wr) {
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
        weatherRecyclerAdapter = new WeatherRecyclerAdapter(new LinkedList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(weatherRecyclerAdapter);

    }


}
