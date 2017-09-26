package com.example.dmitro.weatherapp.network.geoService;


import com.example.dmitro.weatherapp.data.model.geo.placeDetails.PlaceDetails;
import com.example.dmitro.weatherapp.data.model.geo.places.Places;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by dmitro on 25.09.17.
 */


public interface GeoService {
    @GET("maps/api/place/autocomplete/json?sensor=false&types=(cities)")
    public Call<Places> getPlaces(@Query("input") String place);

    @GET("/maps/api/place/details/json")
    public Call<PlaceDetails> getPlaceDetail(@Query("placeid") String id);

}
