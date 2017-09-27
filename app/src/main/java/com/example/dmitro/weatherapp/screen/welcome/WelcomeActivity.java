package com.example.dmitro.weatherapp.screen.welcome;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.dmitro.weatherapp.R;
import com.example.dmitro.weatherapp.screen.current_weather.WeatherDetailsActivity;
import com.example.dmitro.weatherapp.screen.select_city.CityChoiceActivity;
import com.example.dmitro.weatherapp.utils.MyUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class WelcomeActivity extends AppCompatActivity implements WelcomeScreenContract.WelcomeScreenView {
    private final String LOG_TAG="WelcomeActivity_TAG";
    private WelcomeScreenContract.WelcomeScreenPresenter presenter;
    private FusedLocationProviderClient locationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Log.d(LOG_TAG, "tag_day: : "+ MyUtil.getNameDayForDate("2017-09-27 00:00:00"));
        init();
    }

    private void init() {
        presenter = new WelcomeScreenPresenter(this, this);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);


    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.current_weather_btn:
                getWeather();
                break;
            case R.id.search_city_btn:
                startActivity(new Intent(this, CityChoiceActivity.class));
                break;
        }

    }

    private void getWeather() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                presenter.openWeatherActivity(location);


            }
        });
    }


    @Override
    public void setPresenter(WelcomeScreenContract.WelcomeScreenPresenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void openWeatherDetails(double lat, double lon) {
        Intent intent = new Intent(this, WeatherDetailsActivity.class);
        intent.putExtra("lat", lat);
        intent.putExtra("lon", lon);

        startActivity(intent);
    }
}
