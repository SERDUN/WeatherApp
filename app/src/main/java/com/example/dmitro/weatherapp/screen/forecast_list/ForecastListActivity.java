package com.example.dmitro.weatherapp.screen.forecast_list;

import android.os.Bundle;
import android.view.ViewTreeObserver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.data.model.weather.current.WeatherResponse;
import com.example.dmitro.weatherapp.databinding.ActivityForecasListBinding;
import com.example.dmitro.weatherapp.utils.MyUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class ForecastListActivity extends AppCompatActivity implements ForecastListContract.View {

    private ActivityForecasListBinding binding;
    private WeatherForecastRecyclerAdapter weatherForecastRecyclerAdapter;
    private ForecastListContract.Presenter presenter;

    @Override
    @SuppressWarnings({"unchecked", "deprecation"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForecasListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        applyBlur();
        HashMap<String, ArrayList<WeatherResponse>> weather =
                (HashMap<String, ArrayList<WeatherResponse>>) getIntent().getSerializableExtra(getString(R.string.weather_forecast_for_day));
        new ForecastListPresenter(weather == null ? new HashMap<>() : weather, this);
        presenter.init();
    }

    private void applyBlur() {
        binding.backgForecastWeatherActivityIv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                binding.backgForecastWeatherActivityIv.getViewTreeObserver().removeOnPreDrawListener(this);
                MyUtil.blur(binding.backgForecastWeatherActivityIv, ForecastListActivity.this);
                return true;
            }
        });
    }

    private void initView() {
        weatherForecastRecyclerAdapter = new WeatherForecastRecyclerAdapter(new ArrayList<>());
        binding.forecastWeatherRv.setLayoutManager(new LinearLayoutManager(this));
        binding.forecastWeatherRv.setAdapter(weatherForecastRecyclerAdapter);
        binding.toolbar.backBtn.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
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
        binding.toolbar.toolbarTitle.setText(name);
    }
}
