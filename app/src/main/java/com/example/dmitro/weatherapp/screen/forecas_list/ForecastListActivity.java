package com.example.dmitro.weatherapp.screen.forecas_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.data.model.weather.WeatherResponse;
import com.example.dmitro.weatherapp.utils.MyUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForecastListActivity extends AppCompatActivity implements ForecastListContract.View {
    public final static String KEY_WEATHER = "ForecastListActivityWeather";
    private static final float BLUR_RADIUS = 25;

    @BindView(R.id.forecast_weather_rv)
    RecyclerView recyclerView;

    private WeatherForecastRecyclerAdapter weatherForecastRecyclerAdapter;
    private ForecastListContract.Presenter presenter;

    @BindView(R.id.backg_forecast_weather_activity_iv)
    public ImageView backgroundWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecas_list);
        ButterKnife.bind(this);
        initView();
        MyUtil.applyBlur(backgroundWeather, this, BLUR_RADIUS);

        Intent intent = getIntent();
        new ForecastListPresenter((HashMap<String, ArrayList<WeatherResponse>>) intent.getSerializableExtra(KEY_WEATHER), this);
        presenter.init();
    }

    private void initView() {
        weatherForecastRecyclerAdapter = new WeatherForecastRecyclerAdapter(new ArrayList<WeatherResponse>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(weatherForecastRecyclerAdapter);

    }

    @Override
    public void setPresenter(ForecastListContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showWeather(ArrayList<WeatherResponse> weatherResponses) {
        weatherForecastRecyclerAdapter.updateData(weatherResponses);
    }

    @Override
    public void showDayInToolbar(String name) {
        int f = 4;
    }
}
