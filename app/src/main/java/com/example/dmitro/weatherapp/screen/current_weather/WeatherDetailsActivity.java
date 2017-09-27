package com.example.dmitro.weatherapp.screen.current_weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.data.model.weather.WeatherResponse;
import com.example.dmitro.weatherapp.screen.current_weather.fragment.WeatherDetailsFragment;
import com.example.dmitro.weatherapp.screen.current_weather.fragment.WeatherListFragment;
import com.example.dmitro.weatherapp.screen.failure.FailureDialog;
import com.example.dmitro.weatherapp.utils.MyUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherDetailsActivity extends AppCompatActivity implements WeatherScreenContract.WeatherScreenView {

    private final String LOG_WEATHER_ACTIVITY = "WeatherDetailsActivity";

    private WeatherScreenContract.WeatherScreenPresenter presenter;
//
//    @BindView(R.id.weather_details_tb)
//    public Toolbar toolbar;

    public TextView cityNameByToolbar;

    @BindView(R.id.toolbar_title)
    public TextView title_tv;


    @BindView(R.id.backg_weather_details_activity_iv)
    public ImageView backgroundWeather;


    @BindView(R.id.weather_details_pb)
    public ProgressBar progressBar;

    @BindView(R.id.content_weather_details_lv)
    public View contentView;

    @BindView(R.id.back_btn)
    public ImageButton backButton;

    private FailureDialog failureDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        new WeatherScreenPresenter(intent.getDoubleExtra("lat", 0), intent.getDoubleExtra("lon", 0), this);
        init();
        initToolbar();
        MyUtil.applyBlur(backgroundWeather, this, BLUR_RADIUS);

    }

    private void initToolbar() {
        backButton.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void init() {
        failureDialog = new FailureDialog();
        failureDialog.setCallback(() -> {
            failureDialog.dismiss();
            presenter.getWeather();
            presenter.getWeatherForFiveDays();
        });

        presenter.init();
        presenter.getWeather();
        presenter.getWeatherForFiveDays();


    }


    @Override
    public void setPresenter(WeatherScreenContract.WeatherScreenPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showProgressBar(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);

        }
    }


    @Override
    public void showContent(boolean show) {
        if (show) {
            contentView.setVisibility(View.VISIBLE);
        } else {
            contentView.setVisibility(View.GONE);

        }

    }


    @Override
    public void showWeatherDetails(WeatherResponse weatherResponse) {
        WeatherDetailsFragment fragment = (WeatherDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment1);
        Bundle bundle = new Bundle();
        bundle.putSerializable(fragment.FRAGMENT_KEY, weatherResponse);
        fragment.setArguments(bundle);
        fragment.notifyField();
    }

    private static final float BLUR_RADIUS = 25f;


    @Override
    public void showCityNameInToolbar(String name) {
        title_tv.setText(name);
    }

    @Override
    public void showFailure() {
        contentView.setVisibility(View.GONE);
        failureDialog.show(getSupportFragmentManager(), LOG_WEATHER_ACTIVITY);

    }

    @Override
    public void showWeatherList(ArrayList<HashMap<String, ArrayList<WeatherResponse>>> responseManyDayWeather) {
        WeatherListFragment fragment = (WeatherListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment2);
        Bundle bundle = new Bundle();
        bundle.putSerializable(WeatherListFragment.FRAGMENT_KEY, responseManyDayWeather);
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


}
