package com.example.dmitro.weatherapp.data.repository.remote;

import android.os.Bundle;

import com.example.dmitro.weatherapp.data.model.geo.placeDetails.PlaceDetails;
import com.example.dmitro.weatherapp.data.model.geo.places.Places;
import com.example.dmitro.weatherapp.data.model.social.User;
import com.example.dmitro.weatherapp.data.model.weather.current.WeatherResponse;
import com.example.dmitro.weatherapp.data.model.weather.many_day.ResponseManyDayWeather;
import com.example.dmitro.weatherapp.data.repository.SocialDataSources;
import com.example.dmitro.weatherapp.data.repository.WeatherDataSource;
import com.example.dmitro.weatherapp.service.geo_service.GeoFactory;
import com.example.dmitro.weatherapp.service.weather_service.weather_service.WeatherFactory;
import com.example.dmitro.weatherapp.utils.callback.Action0;
import com.example.dmitro.weatherapp.utils.callback.Action1;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dmitro on 25.09.17.
 */

public class RemoteRepository implements WeatherDataSource, SocialDataSources {
    @Override
    public WeatherResponse getCacheWeather() {
        return null;
    }

    @Override
    public ResponseManyDayWeather getCacheManyDayWeather() {
        return null;
    }

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
    public void getWeatherForFiveDay(double lat, double lon, Action1<ResponseManyDayWeather> success, Action1<Throwable> failure, Action0 complete) {
        Call<ResponseManyDayWeather> response = WeatherFactory.getService().getWeatherForManyDay(lat, lon);

        response.enqueue(new Callback<ResponseManyDayWeather>() {
            @Override
            public void onResponse(Call<ResponseManyDayWeather> call, Response<ResponseManyDayWeather> response) {
                success.call(response.body());
                complete.call();
            }

            @Override
            public void onFailure(Call<ResponseManyDayWeather> call, Throwable t) {
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

    @Deprecated
    @Override
    public void cacheUserData(User userFacebook) {

    }

    @Override
    public void getCurrentUser(Action1<User> success, Action1<Throwable> failure, Action0 complete) {

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), (object, response) -> {
            if (object != null) {
                User user=new User();
                try {
                    user.setName(response.getJSONObject().getString("name"));
                    user.setEmail(response.getJSONObject().getString("email"));
                    user.setImageUrl(((JSONObject) ((JSONObject) response.getJSONObject().get("picture")).get("data")).getString("url"));
                success.call(user);
                complete.call();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                failure.call(new Throwable("Not valid access token"));
                complete.call();
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,link,picture.type(large),email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }
}
