package com.example.dmitro.weatherapp.screen.weatherScreen;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.data.model.weather.WeatherResponse;
import com.example.dmitro.weatherapp.screen.weatherScreen.fragment.FailureFragment;
import com.example.dmitro.weatherapp.screen.weatherScreen.fragment.WeatherDetailsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherDetailsActivity extends AppCompatActivity implements WeatherScreenContract.WeatherScreenView {

    private final String LOG_WEATHER_ACTIVITY = "WeatherDetailsActivity";

    private WeatherScreenContract.WeatherScreenPresenter presenter;
    private Fragment[] fragments;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        new WeatherScreenPresenter(intent.getDoubleExtra("lat", 0), intent.getDoubleExtra("lon", 0), this);
        init();

    }

    private void init() {

        fragments = new Fragment[2];
        fragments[0] = new WeatherDetailsFragment();
        fragments[1] = new FailureFragment();
        presenter.getWeather();

    }



    @Override
    public void setPresenter(WeatherScreenContract.WeatherScreenPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showWeatherDetails(WeatherResponse weatherResponse) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(((WeatherDetailsFragment) fragments[0]).FRAGMENT_KEY, weatherResponse);
        fragments[0].setArguments(bundle);
        showFragment(fragments[0], WeatherDetailsFragment.FRAGMENT_KEY);
    }

    @Override
    public void showFailureMessage() {
        showFragment(fragments[1], FailureFragment.FRAGMENT_KEY);

    }

    private void showFragment(Fragment fragment, String fragmentKey) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(fragmentKey) == null) {
            fragmentManager.beginTransaction().replace(R.id.containerFragment, fragment, fragmentKey).commit();
        }

    }
}
