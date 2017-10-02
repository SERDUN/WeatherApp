package com.example.dmitro.weatherapp.screen.forecast_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.data.model.weather.current.WeatherResponse;
import com.example.dmitro.weatherapp.utils.MyUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForecastListActivity extends AppCompatActivity implements ForecastListContract.View {

    @BindView(R.id.forecast_weather_rv)
    RecyclerView recyclerView;

    private WeatherForecastRecyclerAdapter weatherForecastRecyclerAdapter;
    private ForecastListContract.Presenter presenter;

    @BindView(R.id.backg_forecast_weather_activity_iv)
    public ImageView backgroundWeather;

    @BindView(R.id.toolbar_title)
    TextView titleToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecas_list);
        ButterKnife.bind(this);
        initView();
        applyBlur();
        Intent intent = getIntent();
        new ForecastListPresenter((HashMap<String, ArrayList<WeatherResponse>>) intent.getSerializableExtra(getString(R.string.weather_forecast_for_day)), this);
        presenter.init();
    }

    private void applyBlur() {
        backgroundWeather.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                backgroundWeather.getViewTreeObserver().removeOnPreDrawListener(this);
                MyUtil.blur(backgroundWeather, getBaseContext());
                return true;
            }
        });

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
        titleToolbar.setText(name);
    }

    @OnClick(R.id.back_btn)
    public void onClick() {
        onBackPressed();
    }
}
