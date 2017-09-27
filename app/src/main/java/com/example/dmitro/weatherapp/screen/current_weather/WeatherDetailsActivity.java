package com.example.dmitro.weatherapp.screen.current_weather;

import android.content.Intent;

import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.data.model.weather.WeatherResponse;
import com.example.dmitro.weatherapp.screen.current_weather.fragment.WeatherDetailsFragment;
import com.example.dmitro.weatherapp.screen.current_weather.fragment.WeatherListFragment;
import com.example.dmitro.weatherapp.utils.MyUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherDetailsActivity extends AppCompatActivity implements WeatherScreenContract.WeatherScreenView {

    private final String LOG_WEATHER_ACTIVITY = "WeatherDetailsActivity";

    private WeatherScreenContract.WeatherScreenPresenter presenter;

    @BindView(R.id.weather_details_tb)
    public Toolbar toolbar;

    public TextView cityNameByToolbar;


    @BindView(R.id.backg_weather_details_activity_iv)
    public ImageView backgroundWeather;


    @BindView(R.id.weather_details_pb)
    public ProgressBar progressBar;

    @BindView(R.id.content_weather_details_lv)
    public View contentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        new WeatherScreenPresenter(intent.getDoubleExtra("lat", 0), intent.getDoubleExtra("lon", 0), this);
        showProgresBar(true);
        init();
        initToolbar();
        MyUtil.applyBlur(backgroundWeather, this, BLUR_RADIUS);

    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        cityNameByToolbar = toolbar.findViewById(R.id.toolbar_title);
        if (getSupportActionBar() != null) {
            toolbar.getBackground().setAlpha(17);
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
    }

    private void init() {
        presenter.getWeather();
        presenter.getWeatherForFiveDays();


    }


    @Override
    public void setPresenter(WeatherScreenContract.WeatherScreenPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showWeatherDetails(WeatherResponse weatherResponse) {
        WeatherDetailsFragment fragment = (WeatherDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment1);
        Bundle bundle = new Bundle();
        bundle.putSerializable(fragment.FRAGMENT_KEY, weatherResponse);
        fragment.setArguments(bundle);
        fragment.notifyField();
        showProgresBar(false);
    }

    private static final float BLUR_RADIUS = 25f;


    @Override
    public void showCityNameInToolbar(String name) {
        cityNameByToolbar.setText(name);
    }

    @Override
    public void showWeatherList(ArrayList<HashMap<String, ArrayList<WeatherResponse>>> responseManyDayWeather) {
        WeatherListFragment fragment = (WeatherListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);
        Bundle bundle = new Bundle();
        bundle.putSerializable(fragment.FRAGMENT_KEY, responseManyDayWeather);
        fragment.setArguments(bundle);
        fragment.notifyField();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    private void showProgresBar(boolean is) {
        if (is) {
            progressBar.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            contentView.setVisibility(View.VISIBLE);
        }
    }


}
