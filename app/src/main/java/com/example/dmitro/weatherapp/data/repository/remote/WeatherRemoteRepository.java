package com.example.dmitro.weatherapp.data.repository.remote;

import com.example.dmitro.weatherapp.data.model.geo.placeDetails.PlaceDetails;
import com.example.dmitro.weatherapp.data.model.geo.places.Places;
import com.example.dmitro.weatherapp.data.model.weather.WeatherResponse;
import com.example.dmitro.weatherapp.data.repository.WeatherDataSource;
import com.example.dmitro.weatherapp.network.geoService.GeoFactory;
import com.example.dmitro.weatherapp.network.geoService.GeoService;
import com.example.dmitro.weatherapp.network.weatherService.WeatherFactory;
import com.example.dmitro.weatherapp.utils.callback.Action0;
import com.example.dmitro.weatherapp.utils.callback.Action1;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dmitro on 25.09.17.
 */

public class WeatherRemoteRepository implements WeatherDataSource {
    @Override
    public void getCurrentWeather(double lat, double lon, final Action1<WeatherResponse> success, final Action1<Throwable> failure, final Action0 complete) {
        Call<WeatherResponse> call = WeatherFactory.getService().getCurrentWeather(lat, lon);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                success.call(response.body());
                complete.call();
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                failure.call(t);
                complete.call();
            }
        });
    }

    @Override
    public void getPlaceByName(String name, Action1<Places> success, Action1<Throwable> failure, Action0 complete) {
        Call<Places> call = GeoFactory.getService().getPlaces(name);
        call.enqueue(new Callback<Places>() {
            @Override
            public void onResponse(Call<Places> call, Response<Places> response) {
                success.call(response.body());
                complete.call();

            }

            @Override
            public void onFailure(Call<Places> call, Throwable t) {
                failure.call(t);
                complete.call();

            }
        });
    }

    @Override
    public void getPlaceId(String placeId, Action1<PlaceDetails> success, Action1<Throwable> failure, Action0 complete) {
        Call<PlaceDetails> call = GeoFactory.getService().getPlaceDetail(placeId);
        call.enqueue(new Callback<PlaceDetails>() {
            @Override
            public void onResponse(Call<PlaceDetails> call, Response<PlaceDetails> response) {
                success.call(response.body());
                complete.call();
            }

            @Override
            public void onFailure(Call<PlaceDetails> call, Throwable t) {
                failure.call(t);
                complete.call();
            }
        });
    }
}
